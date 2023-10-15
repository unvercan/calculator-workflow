package tr.unvercanunlu.calculator_workflow.service.workflow.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(CalculatorWorkflow.class);

    private final ICalculationActivity calculationActivity = WorkflowConfig.getCalculationActivity();

    private final IOperandActivity operandActivity =
            Workflow.newActivityStub(IOperandActivity.class, WorkflowConfig.Options.Activity.OPERAND);

    private final IOperandActivity operandActivity = WorkflowConfig.getOperandActivity();

    private final IResultActivity resultActivity = WorkflowConfig.getResultActivity();

    @Override
    public Calculation process(CreateCalculationRequest request) {
        this.logger.info("Calculation Process in Calculator Workflow is started.");

        Calculation calculation = this.calculationActivity.create(request);

        CreateOperandRequest createOperandRequest = CreateOperandRequest.builder()
                .first(request.getFirst())
                .second(request.getSecond())
                .calculationId(calculation.getId())
                .build();

        Operand operand = this.operandActivity.create(createOperandRequest);

        this.calculationActivity.setOperand(calculation.getId(), operand.getId());

        calculation.setOperand(operand);

        CreateResultRequest createResultRequest = CreateResultRequest.builder()
                .operandId(operand.getId())
                .operationCode(request.getOperationCode())
                .calculationId(calculation.getId())
                .build();

        Result result = this.resultActivity.create(createResultRequest);

        this.calculationActivity.setResult(calculation.getId(), result.getId());

        calculation.setResult(result);

        this.calculationActivity.setDone(calculation.getId(), Boolean.TRUE);

        calculation.setDone(Boolean.TRUE);

        this.logger.info("Process in Calculator Workflow is completed.");

        return calculation;
    }
}
