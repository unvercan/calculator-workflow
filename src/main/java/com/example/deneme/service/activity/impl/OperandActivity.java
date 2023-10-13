package com.example.deneme.service.activity.impl;

import com.example.deneme.model.entity.Operand;
import com.example.deneme.model.request.CreateOperandRequest;
import com.example.deneme.repository.IOperandRepository;
import com.example.deneme.service.activity.IOperandActivity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OperandActivity implements IOperandActivity {

    private final Logger logger = LoggerFactory.getLogger(OperandActivity.class);

    private final IOperandRepository operandRepository;

    @Override
    public Operand createOperand(CreateOperandRequest request) {
        Operand operand = Operand.builder()
                .first(request.getFirst())
                .second(request.getSecond())
                .calculationId(request.getCalculationId())
                .build();

        this.logger.info("Operand is created: " + operand);

        operand = this.operandRepository.save(operand);

        this.logger.info("Operand is saved: " + operand);

        return operand;
    }

}
