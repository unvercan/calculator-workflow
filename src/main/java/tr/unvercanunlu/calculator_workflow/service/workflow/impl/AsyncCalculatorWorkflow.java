package tr.unvercanunlu.calculator_workflow.service.workflow.impl;

import io.temporal.workflow.Async;
import io.temporal.workflow.Promise;
import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import tr.unvercanunlu.calculator_workflow.model.entity.Calculation;
import tr.unvercanunlu.calculator_workflow.model.request.CreateCalculationRequest;
import tr.unvercanunlu.calculator_workflow.model.request.CreateOperandRequest;
import tr.unvercanunlu.calculator_workflow.model.request.CreateResultRequest;
import tr.unvercanunlu.calculator_workflow.service.WorkflowConfig;
import tr.unvercanunlu.calculator_workflow.service.activity.ICalculationActivity;
import tr.unvercanunlu.calculator_workflow.service.activity.IOperandActivity;
import tr.unvercanunlu.calculator_workflow.service.activity.IResultActivity;
import tr.unvercanunlu.calculator_workflow.service.workflow.IAsyncCalculatorWorkflow;

import java.time.Duration;

public class AsyncCalculatorWorkflow implements IAsyncCalculatorWorkflow {

    private final Logger workflowLogger = Workflow.getLogger(AsyncCalculatorWorkflow.class);

    private final ICalculationActivity calculationActivity = WorkflowConfig.getCalculationActivity();

    private final IOperandActivity operandActivity = WorkflowConfig.getOperandActivity();

    private final IResultActivity resultActivity = WorkflowConfig.getResultActivity();

    @Override
    public Calculation processAsync(CreateCalculationRequest request) {
        this.workflowLogger.info("Async Process in Async Calculator Workflow is started.");

        this.workflowLogger.info("Async Execution of Create in Calculation Activity is starting.");

        Promise<Calculation> calculationPromise = Async.function(this.calculationActivity::create, request).thenCompose(calculation -> {
            this.workflowLogger.info("Async Execution of Create in Calculation Activity is done.");

            CreateOperandRequest createOperandRequest = CreateOperandRequest.builder()
                    .first(request.getFirst())
                    .second(request.getSecond())
                    .calculationId(calculation.getId())
                    .build();

            this.workflowLogger.info("Async Execution of Create in Operand Activity is starting.");

            return Async.function(this.operandActivity::create, createOperandRequest).thenCompose(operand -> {
                this.workflowLogger.info("Async Execution of Create in Operand Activity is done.");

                CreateResultRequest createResultRequest = CreateResultRequest.builder()
                        .operandId(operand.getId())
                        .operationCode(request.getOperationCode())
                        .calculationId(calculation.getId())
                        .build();

                this.workflowLogger.info("Async Execution of Create in Result Activity is starting.");

                return Async.function(this.resultActivity::create, createResultRequest).thenCompose(result -> {
                            this.workflowLogger.info("Async Execution of Create in Result Activity is done.");

                            this.workflowLogger.info("Async Execution of Set Operand in Calculation Activity is starting.");
                            this.workflowLogger.info("Async Execution of Set Result in Calculation Activity is starting.");
                            this.workflowLogger.info("Async Execution of Set Completeness in Calculation Activity is starting.");

                            this.workflowLogger.info("Async Executions of Set Operand, Set Result, and Set Completeness in Calculation Activity are combined.");

                            // combined promise
                            return Promise.allOf(
                                    Async.procedure(this.calculationActivity::setOperand, calculation.getId(), operand.getId()),
                                    Async.procedure(this.calculationActivity::setResult, calculation.getId(), result.getId()),
                                    Async.procedure(this.calculationActivity::setCompleteness, calculation.getId(), Boolean.TRUE)
                            ).thenApply(ignored -> {
                                this.workflowLogger.info("Combined async executions of Set Operand, Set Result, and Set Completeness in Calculation Activity are done.");

                                calculation.setOperand(operand);
                                calculation.setResult(result);
                                calculation.setDone(Boolean.TRUE);

                                return calculation;
                            });
                        }
                );
            });
        });

        while (!calculationPromise.isCompleted()) {
            Workflow.sleep(Duration.ofMillis(200).toMillis());

            this.workflowLogger.info("waiting for execution of Async Process in Async Calculation Workflow.");
        }

        this.workflowLogger.info("Async Process in Async Calculator Workflow is completed.");

        return calculationPromise.get();
    }
}
