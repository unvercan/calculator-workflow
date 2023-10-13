package com.example.deneme.service.workflow;

import com.example.deneme.model.entity.Calculation;
import com.example.deneme.model.request.CreateCalculationRequest;
import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;

@WorkflowInterface
public interface ICalculatorWorkflow {

    @WorkflowMethod
    Calculation process(CreateCalculationRequest request);

}
