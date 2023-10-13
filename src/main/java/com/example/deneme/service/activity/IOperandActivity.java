package com.example.deneme.service.activity;

import com.example.deneme.model.entity.Operand;
import com.example.deneme.model.request.CreateOperandRequest;
import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;

import java.util.UUID;

@ActivityInterface
public interface IOperandActivity {

    @ActivityMethod
    Operand createOperand(CreateOperandRequest request);

}
