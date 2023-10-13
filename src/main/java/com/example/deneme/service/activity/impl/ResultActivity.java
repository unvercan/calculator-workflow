package com.example.deneme.service.activity.impl;

import com.example.deneme.model.entity.Operand;
import com.example.deneme.model.entity.Operation;
import com.example.deneme.model.entity.Result;
import com.example.deneme.model.request.CreateResultRequest;
import com.example.deneme.repository.IOperandRepository;
import com.example.deneme.repository.IOperationRepository;
import com.example.deneme.repository.IResultRepository;
import com.example.deneme.service.activity.IResultActivity;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResultActivity implements IResultActivity {

    private final Logger logger = LoggerFactory.getLogger(ResultActivity.class);

    private final IOperandRepository operandRepository;

    private final IOperationRepository operationRepository;

    private final IResultRepository resultRepository;

    @Override
    public Result createResult(CreateResultRequest request) {
        Operation operation = this.operationRepository.findById(request.getOperationCode()).get();

        this.logger.info("Operation is retrieved: " + operation);

        Operand operand = this.operandRepository.findById(request.getOperandId()).get();

        this.logger.info("Operand is retrieved: " + operand);

        Double third = switch (operation.getCode()) {
            case 0 -> (double) operand.getFirst() + (double) operand.getSecond();
            case 1 -> (double) operand.getFirst() - (double) operand.getSecond();
            case 2 -> (double) operand.getFirst() * (double) operand.getSecond();
            case 3 -> (double) operand.getFirst() / (double) operand.getSecond();
            case 4 -> (double) operand.getFirst() % (double) operand.getSecond();
            case 5 -> Math.pow(operand.getFirst(), operand.getSecond());
            case 6 -> ((double) operand.getFirst() + (double) operand.getSecond()) / 2;
            case 7 -> (double) Math.max(operand.getFirst(), operand.getSecond());
            case 8 -> (double) Math.min(operand.getFirst(), operand.getSecond());
            default -> null;
        };

        Result result = Result.builder()
                .value(third)
                .calculationId(request.getCalculationId())
                .build();

        this.logger.info("Result is created: " + result);

        result = this.resultRepository.save(result);

        this.logger.info("Result is saved: " + result);

        return result;
    }

}
