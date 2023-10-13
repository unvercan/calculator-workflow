package com.example.deneme.service.workflow.impl;

import com.example.deneme.model.entity.Calculation;
import com.example.deneme.model.entity.Operand;
import com.example.deneme.model.entity.Result;
import com.example.deneme.model.request.CreateCalculationRequest;
import com.example.deneme.model.request.CreateOperandRequest;
import com.example.deneme.model.request.CreateResultRequest;
import com.example.deneme.service.WorkflowConfig;
import com.example.deneme.service.activity.ICalculationActivity;
import com.example.deneme.service.activity.IOperandActivity;
import com.example.deneme.service.activity.IResultActivity;
import com.example.deneme.service.workflow.ICalculatorWorkflow;
import io.temporal.workflow.Workflow;

public class CalculatorWorkflow implements ICalculatorWorkflow {

    private final ICalculationActivity calculationActivity =
            Workflow.newActivityStub(ICalculationActivity.class, WorkflowConfig.Options.Activity.CALCULATION);

    private final IOperandActivity operandActivity =
            Workflow.newActivityStub(IOperandActivity.class, WorkflowConfig.Options.Activity.OPERAND);

    private final IResultActivity resultActivity =
            Workflow.newActivityStub(IResultActivity.class, WorkflowConfig.Options.Activity.RESULT);

    @Override
    public Calculation process(CreateCalculationRequest request) {
        Calculation calculation = this.calculationActivity.createCalculation(request);

        CreateOperandRequest createOperandRequest = CreateOperandRequest.builder()
                .first(request.getFirst())
                .second(request.getSecond())
                .calculationId(calculation.getId())
                .build();

        Operand operand = this.operandActivity.createOperand(createOperandRequest);

        calculation = this.calculationActivity.setOperandForCalculation(calculation.getId(), operand.getId());

        CreateResultRequest createResultRequest = CreateResultRequest.builder()
                .operandId(operand.getId())
                .operationCode(request.getOperationCode())
                .build();

        Result result = this.resultActivity.createResult(createResultRequest);

        calculation = this.calculationActivity.setResultForCalculation(calculation.getId(), result.getId());

        return this.calculationActivity.setDoneForCalculation(calculation.getId(), Boolean.TRUE);
    }
}
