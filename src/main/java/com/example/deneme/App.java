package com.example.deneme;

import com.example.deneme.service.WorkflowConfig;
import com.example.deneme.service.activity.ICalculationActivity;
import com.example.deneme.service.activity.IOperandActivity;
import com.example.deneme.service.activity.IResultActivity;
import com.example.deneme.service.workflow.ICalculatorWorkflow;
import com.example.deneme.service.workflow.impl.CalculatorWorkflow;
import io.temporal.worker.Worker;
import io.temporal.worker.WorkerFactory;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class App {

    private final ICalculationActivity calculationActivity;

    private final IOperandActivity operandActivity;

    private final IResultActivity resultActivity;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    private void createAndRegisterWorkerForCalculatorWorkflow(WorkerFactory factory) {
        Worker worker = factory.newWorker(WorkflowConfig.TaskQueue.CALCULATOR);
        worker.registerWorkflowImplementationFactory(ICalculatorWorkflow.class, CalculatorWorkflow::new);
    }

    private void createAndRegisterWorkerForCalculationActivity(WorkerFactory factory, ICalculationActivity activity) {
        Worker worker = factory.newWorker(WorkflowConfig.TaskQueue.CALCULATION);
        worker.registerActivitiesImplementations(activity);
    }

    private void createAndRegisterWorkerForOperandActivity(WorkerFactory factory, IOperandActivity activity) {
        Worker worker = factory.newWorker(WorkflowConfig.TaskQueue.OPERAND);
        worker.registerActivitiesImplementations(activity);
    }

    private void createAndRegisterWorkerForResultActivity(WorkerFactory factory, IResultActivity activity) {
        Worker worker = factory.newWorker(WorkflowConfig.TaskQueue.RESULT);
        worker.registerActivitiesImplementations(activity);
    }

    @PostConstruct
    public void createWorker() {
        WorkerFactory workerFactory = WorkerFactory.newInstance(WorkflowConfig.getClient());

        this.createAndRegisterWorkerForCalculatorWorkflow(workerFactory);

        this.createAndRegisterWorkerForCalculationActivity(workerFactory, this.calculationActivity);

        this.createAndRegisterWorkerForOperandActivity(workerFactory, this.operandActivity);

        this.createAndRegisterWorkerForResultActivity(workerFactory, this.resultActivity);

        workerFactory.start();
    }

}
