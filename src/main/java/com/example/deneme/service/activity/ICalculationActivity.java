package com.example.deneme.service.activity;

import com.example.deneme.model.entity.Calculation;
import com.example.deneme.model.request.CreateCalculationRequest;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface ICalculationActivity {

    @ActivityMethod
    Calculation createCalculation(CreateCalculationRequest request);

    @ActivityMethod
    Calculation setOperandForCalculation(UUID calculationId, UUID operandId);

    @ActivityMethod
    Calculation setResultForCalculation(UUID calculationId, UUID resultId);

    @ActivityMethod
    Calculation setDoneForCalculation(UUID id, Boolean done);

}
