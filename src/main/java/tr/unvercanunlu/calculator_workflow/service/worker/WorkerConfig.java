package tr.unvercanunlu.calculator_workflow.service.worker;

import io.temporal.client.WorkflowClient;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import tr.unvercanunlu.calculator_workflow.service.WorkflowConfig;
import tr.unvercanunlu.calculator_workflow.service.activity.ICalculationActivity;
import tr.unvercanunlu.calculator_workflow.service.activity.IOperandActivity;
import tr.unvercanunlu.calculator_workflow.service.activity.IResultActivity;
import tr.unvercanunlu.calculator_workflow.service.workflow.ICalculatorWorkflow;
import tr.unvercanunlu.calculator_workflow.service.workflow.impl.CalculatorWorkflow;

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

        Worker workflowWorker = workerFactory.newWorker(WorkflowConfig.TaskQueue.CALCULATOR);

        this.logger.info("Worker for " + WorkflowConfig.TaskQueue.CALCULATOR + " Task Queue is created using Worker Factory.");

        workflowWorker.registerWorkflowImplementationFactory(ICalculatorWorkflow.class, CalculatorWorkflow::new);

        this.logger.info("Worker for " + WorkflowConfig.TaskQueue.CALCULATOR + " Task Queue is registered for Calculator Workflow to Worker Factory.");

        Map<String, Object> taskQueueActivityMap = new HashMap<>() {{
            put(WorkflowConfig.TaskQueue.CALCULATION, calculationActivity);
            put(WorkflowConfig.TaskQueue.OPERAND, operandActivity);
            put(WorkflowConfig.TaskQueue.RESULT, resultActivity);
        }};

        Map<Object, Worker> activityWorkerMap = taskQueueActivityMap.entrySet()
                .stream()
                .map(entry -> {
                    String taskQueue = entry.getKey();
                    Object activity = entry.getValue();

                    Worker activityWorker = workerFactory.newWorker(taskQueue);

                    this.logger.info("Worker for " + taskQueue + " Task Queue is created using Worker Factory.");

                    return Map.entry(activity, activityWorker);
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        taskQueueActivityMap.forEach((taskQueue, activity) -> {
            Worker activityWorker = activityWorkerMap.get(activity);

            activityWorker.registerActivitiesImplementations(activity);

            this.logger.info("Worker for " + taskQueue + " Task Queue is registered for " + activity.getClass().getSimpleName() + " to Worker Factory.");
        });

        this.logger.info("Creation of Workers using Worker Factory is completed.");

        workerFactory.start();

        this.logger.debug("Workers are started using Worker Factory.");
    }
}
