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

@Configuration
@RequiredArgsConstructor
public class WorkerConfig {

    private final Logger logger = LoggerFactory.getLogger(WorkerConfig.class);

    private final ICalculationActivity calculationActivity;

    private final IOperandActivity operandActivity;

    private final IResultActivity resultActivity;

    private void createAndRegisterWorkerForCalculatorWorkflow(WorkerFactory factory) {
        Worker worker = factory.newWorker(WorkflowConfig.TaskQueue.CALCULATOR);

        this.logger.info("Worker for " + WorkflowConfig.TaskQueue.CALCULATOR + " Task Queue is created.");

        worker.registerWorkflowImplementationFactory(ICalculatorWorkflow.class, CalculatorWorkflow::new);

        this.logger.info("Worker for " + WorkflowConfig.TaskQueue.CALCULATOR + " Task Queue is registered for Calculator Workflow.");
    }

    private void createAndRegisterActivityWorker(WorkerFactory factory, String taskQueue, Object activity) {
        Worker worker = factory.newWorker(taskQueue);

        this.logger.info("Worker for " + taskQueue + " Task Queue is created.");

        worker.registerActivitiesImplementations(activity);

        this.logger.info("Worker for " + taskQueue + " Task Queue is registered for " + activity.getClass().getSimpleName() + ".");
    }

    @PostConstruct
    public void createWorkers() {
        WorkflowClient client = WorkflowConfig.getClient();

        WorkerFactory workerFactory = WorkerFactory.newInstance(client);

        this.logger.info("Creation of Workers using Worker Factory is starting.");

        this.createAndRegisterWorkerForCalculatorWorkflow(workerFactory);

        this.createAndRegisterActivityWorker(workerFactory, WorkflowConfig.TaskQueue.CALCULATION, this.calculationActivity);
        this.createAndRegisterActivityWorker(workerFactory, WorkflowConfig.TaskQueue.OPERAND, this.operandActivity);
        this.createAndRegisterActivityWorker(workerFactory, WorkflowConfig.TaskQueue.RESULT, this.resultActivity);

        this.logger.info("Creation of Workers using Worker Factory is completed.");

        workerFactory.start();

        this.logger.debug("Workers are started.");
    }
}
