package tr.unvercanunlu.calculator_workflow.service.worker;

import io.temporal.client.WorkflowClient;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import io.temporal.workflow.Functions;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import tr.unvercanunlu.calculator_workflow.service.WorkflowConfig;
import tr.unvercanunlu.calculator_workflow.service.activity.ICalculationActivity;
import tr.unvercanunlu.calculator_workflow.service.activity.IOperandActivity;
import tr.unvercanunlu.calculator_workflow.service.activity.IResultActivity;
import tr.unvercanunlu.calculator_workflow.service.workflow.IAsyncCalculatorWorkflow;
import tr.unvercanunlu.calculator_workflow.service.workflow.ICalculatorWorkflow;
import tr.unvercanunlu.calculator_workflow.service.workflow.impl.AsyncCalculatorWorkflow;
import tr.unvercanunlu.calculator_workflow.service.workflow.impl.CalculatorWorkflow;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@RequiredArgsConstructor
public class WorkerConfig {

    private final Logger logger = LoggerFactory.getLogger(WorkerConfig.class);

    private final ICalculationActivity calculationActivity;

    private final IOperandActivity operandActivity;

    private final IResultActivity resultActivity;

    @PostConstruct
    public void createWorkers() {
        WorkflowClient client = WorkflowConfig.getClient();

        WorkerFactory workerFactory = WorkerFactory.newInstance(client);

        this.logger.info("Worker Factory is created using Workflow Client.");

        this.logger.info("Creation of Workers using Worker Factory is starting.");

        Map<String, AbstractMap.SimpleEntry<Class, Functions.Func>> taskQueueWorkflowMap = new HashMap<>() {{
            put(WorkflowConfig.TaskQueue.CALCULATOR,
                    new SimpleEntry<>(ICalculatorWorkflow.class, CalculatorWorkflow::new));
            put(WorkflowConfig.TaskQueue.ASYNC_CALCULATOR,
                    new SimpleEntry<>(IAsyncCalculatorWorkflow.class, AsyncCalculatorWorkflow::new));
        }};

        Map<String, Worker> taskQueueWorkflowWorkerMap = taskQueueWorkflowMap.keySet().stream().map(taskQueue -> {
            Worker workflowWorker = workerFactory.newWorker(taskQueue);

            this.logger.info("Worker for " + taskQueue + " Task Queue is created using Worker Factory.");

            return Map.entry(taskQueue, workflowWorker);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        taskQueueWorkflowMap.forEach((taskQueue, workflowInterfaceWithGenerator) -> {
            Worker workflowWorker = taskQueueWorkflowWorkerMap.get(taskQueue);

            Class workflowInterface = workflowInterfaceWithGenerator.getKey();

            Functions.Func workflowInstanceGenerator = workflowInterfaceWithGenerator.getValue();

            workflowWorker.registerWorkflowImplementationFactory(workflowInterface, workflowInstanceGenerator);

            this.logger.info("Worker for " + taskQueue + " Task Queue is registered for "
                    + workflowInterface.getClass().getSimpleName() + " to Worker Factory.");
        });

        Map<String, Object> taskQueueActivityInstanceMap = new HashMap<>() {{
            put(WorkflowConfig.TaskQueue.CALCULATION, calculationActivity);
            put(WorkflowConfig.TaskQueue.OPERAND, operandActivity);
            put(WorkflowConfig.TaskQueue.RESULT, resultActivity);
        }};

        Map<String, Worker> taskQueueActivityWorkerMap = taskQueueActivityInstanceMap.keySet().stream().map(taskQueue -> {
            Worker activityWorker = workerFactory.newWorker(taskQueue);

            this.logger.info("Worker for " + taskQueue + " Task Queue is created using Worker Factory.");

            return Map.entry(taskQueue, activityWorker);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        taskQueueActivityInstanceMap.forEach((taskQueue, activityInstance) -> {
            Worker activityWorker = taskQueueActivityWorkerMap.get(taskQueue);

            activityWorker.registerActivitiesImplementations(activityInstance);

            this.logger.info("Worker for " + taskQueue + " Task Queue is registered for "
                    + activityInstance.getClass().getSimpleName() + " to Worker Factory.");
        });

        this.logger.info("Creation of Workers using Worker Factory is completed.");

        workerFactory.start();

        this.logger.debug("Workers are started using Worker Factory.");
    }
}
