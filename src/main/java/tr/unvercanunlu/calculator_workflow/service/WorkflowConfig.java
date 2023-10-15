package tr.unvercanunlu.calculator_workflow.service;

import io.temporal.activity.ActivityOptions;
import io.temporal.client.WorkflowClient;
import io.temporal.client.WorkflowClientOptions;
import io.temporal.client.WorkflowOptions;
import io.temporal.common.RetryOptions;
import io.temporal.serviceclient.WorkflowServiceStubs;
import io.temporal.serviceclient.WorkflowServiceStubsOptions;
import io.temporal.workflow.Workflow;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import tr.unvercanunlu.calculator_workflow.service.activity.ICalculationActivity;
import tr.unvercanunlu.calculator_workflow.service.activity.IOperandActivity;
import tr.unvercanunlu.calculator_workflow.service.activity.IResultActivity;
import tr.unvercanunlu.calculator_workflow.service.workflow.ICalculatorWorkflow;

import java.time.Duration;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WorkflowConfig {

    public static final String URL = "127.0.0.1:7233";

    // public static final String NAMESPACE = "CALCULATOR";

    public static final String ID = "CALCULATOR_WORKFLOW";

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class TaskQueue {

        public static final String CALCULATOR = "CALCULATOR_TASK_QUEUE";

        public static final String CALCULATION = "CALCULATION_TASK_QUEUE";

        public static final String OPERAND = "OPERAND_TASK_QUEUE";

        public static final String RESULT = "RESULT_TASK_QUEUE";

    }

    @NoArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Options {

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Activity {

            public static final ActivityOptions OPERAND =
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofMinutes(1))
                            .setScheduleToCloseTimeout(Duration.ofMinutes(1))
                            .setTaskQueue(WorkflowConfig.TaskQueue.OPERAND)
                            .setRetryOptions(RetryOptions.newBuilder()
                                    .setMaximumAttempts(3)
                                    .build())
                            .build();

            public static final ActivityOptions CALCULATION =
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofMinutes(1))
                            .setScheduleToCloseTimeout(Duration.ofMinutes(1))
                            .setTaskQueue(WorkflowConfig.TaskQueue.CALCULATION)
                            .setRetryOptions(RetryOptions.newBuilder()
                                    .setMaximumAttempts(3)
                                    .build())
                            .build();

            public static final ActivityOptions RESULT =
                    ActivityOptions.newBuilder()
                            .setStartToCloseTimeout(Duration.ofMinutes(1))
                            .setScheduleToCloseTimeout(Duration.ofMinutes(1))
                            .setTaskQueue(WorkflowConfig.TaskQueue.RESULT)
                            .setRetryOptions(RetryOptions.newBuilder()
                                    .setMaximumAttempts(3)
                                    .build())
                            .build();
        }

        @NoArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Workflow {

            public static final WorkflowOptions CALCULATOR =
                    WorkflowOptions.newBuilder()
                            .setWorkflowId(WorkflowConfig.ID)
                            .setTaskQueue(WorkflowConfig.TaskQueue.CALCULATOR)
                            .setWorkflowTaskTimeout(Duration.ofMinutes(1))
                            .setWorkflowRunTimeout(Duration.ofMinutes(1))
                            .setRetryOptions(RetryOptions.newBuilder()
                                    .setMaximumAttempts(3)
                                    .build())
                            .build();
        }

        public static final WorkflowClientOptions CLIENT =
                WorkflowClientOptions.newBuilder()
                        //.setNamespace(WorkflowConfig.NAMESPACE)
                        .build();

        public static final WorkflowServiceStubsOptions SERVICE_STUBS =
                WorkflowServiceStubsOptions.newBuilder()
                        .setTarget(WorkflowConfig.URL)
                        .build();
    }

    public static WorkflowClient getClient() {
        WorkflowServiceStubs serviceStubs = WorkflowServiceStubs.newServiceStubs(WorkflowConfig.Options.SERVICE_STUBS);

        return WorkflowClient.newInstance(serviceStubs, WorkflowConfig.Options.CLIENT);
    }

    public static ICalculatorWorkflow getCalculatorWorkflow() {
        WorkflowClient client = WorkflowConfig.getClient();

        return client.newWorkflowStub(ICalculatorWorkflow.class, WorkflowConfig.Options.Workflow.CALCULATOR);
    }

    public static ICalculationActivity getCalculationActivity() {
        return Workflow.newActivityStub(ICalculationActivity.class, WorkflowConfig.Options.Activity.CALCULATION);
    }

    public static IOperandActivity getOperandActivity() {
        return Workflow.newActivityStub(IOperandActivity.class, WorkflowConfig.Options.Activity.OPERAND);
    }

    public static IResultActivity getResultActivity() {
        return Workflow.newActivityStub(IResultActivity.class, WorkflowConfig.Options.Activity.RESULT);
    }
}
