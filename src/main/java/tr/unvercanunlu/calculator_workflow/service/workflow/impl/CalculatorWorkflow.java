package tr.unvercanunlu.calculator_workflow.service.workflow.impl;

import io.temporal.workflow.Workflow;
import org.slf4j.Logger;
import tr.unvercanunlu.calculator_workflow.model.entity.Calculation;
import tr.unvercanunlu.calculator_workflow.model.entity.Operand;
import tr.unvercanunlu.calculator_workflow.model.entity.Result;
import tr.unvercanunlu.calculator_workflow.model.request.CreateCalculationRequest;
import tr.unvercanunlu.calculator_workflow.model.request.CreateOperandRequest;
import tr.unvercanunlu.calculator_workflow.model.request.CreateResultRequest;
import tr.unvercanunlu.calculator_workflow.service.WorkflowConfig;
import tr.unvercanunlu.calculator_workflow.service.activity.ICalculationActivity;
import tr.unvercanunlu.calculator_workflow.service.activity.IOperandActivity;
import tr.unvercanunlu.calculator_workflow.service.activity.IResultActivity;
import tr.unvercanunlu.calculator_workflow.service.workflow.ICalculatorWorkflow;

public class CalculatorWorkflow implements ICalculatorWorkflow {

    private final Logger workflowLogger = Workflow.getLogger(CalculatorWorkflow.class);

    private final ICalculationActivity calculationActivity = WorkflowConfig.getCalculationActivity();

    private final IOperandActivity operandActivity = WorkflowConfig.getOperandActivity();

    private final IResultActivity resultActivity = WorkflowConfig.getResultActivity();

    @Override
    public Calculation process(CreateCalculationRequest request) {
        this.workflowLogger.info("Process in Calculator Workflow is started.");

        this.workflowLogger.info("Execution of Create in Calculation Activity is starting.");

        Calculation calculation = this.calculationActivity.create(request);

        this.workflowLogger.info("Execution of Create in Calculation Activity is done.");

        CreateOperandRequest createOperandRequest = CreateOperandRequest.builder()
                .first(request.getFirst())
                .second(request.getSecond())
                .calculationId(calculation.getId())
                .build();

        this.workflowLogger.info("Execution of Create in Operand Activity is starting.");

        Operand operand = this.operandActivity.create(createOperandRequest);

        this.workflowLogger.info("Execution of Create in Operand Activity is done.");

        CreateResultRequest createResultRequest = CreateResultRequest.builder()
                .operandId(operand.getId())
                .operationCode(request.getOperationCode())
                .calculationId(calculation.getId())
                .build();

        this.workflowLogger.info("Execution of Create in Result Activity is starting.");

        Result result = this.resultActivity.create(createResultRequest);

        this.workflowLogger.info("Execution of Create in Result Activity is done.");

        this.workflowLogger.info("Execution of Set Operand in Calculation Activity is starting.");

        this.calculationActivity.setOperand(calculation.getId(), operand.getId());

        calculation.setOperand(operand);

        this.workflowLogger.info("Execution of Set Operand in Calculation Activity are done.");

        this.workflowLogger.info("Execution of Set Result in Calculation Activity is starting.");

        this.calculationActivity.setResult(calculation.getId(), result.getId());

        calculation.setResult(result);

        this.workflowLogger.info("Execution of Set Result in Calculation Activity are done.");

        this.workflowLogger.info("Execution of Set Completeness in Calculation Activity is starting.");

        this.calculationActivity.setCompleteness(calculation.getId(), Boolean.TRUE);

        calculation.setDone(Boolean.TRUE);

        this.workflowLogger.info("Execution of Set Completeness in Calculation Activity are done.");

        this.workflowLogger.info("Process in Calculator Workflow is completed.");

        return calculation;
    }
}
