package com.example.deneme.service.activity.impl;

import com.example.deneme.model.entity.Calculation;
import com.example.deneme.model.request.CreateCalculationRequest;
import com.example.deneme.repository.ICalculationRepository;
import com.example.deneme.service.activity.ICalculationActivity;
import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalculationActivity implements ICalculationActivity {

    private final Logger logger = LoggerFactory.getLogger(CalculationActivity.class);

    private final ICalculationRepository calculationRepository;

    @Override
    public Calculation createCalculation(CreateCalculationRequest request) {
        Calculation calculation = Calculation.builder()
                .operationCode(request.getOperationCode())
                .done(Boolean.FALSE)
                .build();

        this.logger.info("Calculation is created: " + calculation);

        calculation = this.calculationRepository.save(calculation);

        this.logger.info("Calculation is saved: " + calculation);

        return calculation;
    }

    @Override
    public Calculation setOperandForCalculation(UUID calculationId, UUID operandId) {
        Calculation calculation = this.calculationRepository.findById(calculationId).get();

        this.logger.info("Calculation is fetched: " + calculation);

        calculation.setOperandId(operandId);

        this.logger.info("Calculation is updated: " + calculation);

        calculation = this.calculationRepository.save(calculation);

        this.logger.info("Calculation is saved: " + calculation);

        return calculation;
    }

    @Override
    public Calculation setResultForCalculation(UUID calculationId, UUID resultId) {
        Calculation calculation = this.calculationRepository.findById(calculationId).get();

        this.logger.info("Calculation is fetched: " + calculation);

        calculation.setResultId(resultId);

        this.logger.info("Calculation is updated: " + calculation);

        calculation = this.calculationRepository.save(calculation);

        this.logger.info("Calculation is saved: " + calculation);

        return calculation;
    }

    @Override
    public Calculation setDoneForCalculation(UUID id, Boolean done) {
        Calculation calculation = this.calculationRepository.findById(id).get();

        this.logger.info("Calculation is fetched: " + calculation);

        calculation.setDone(done);

        this.logger.info("Calculation is updated: " + calculation);

        calculation = this.calculationRepository.save(calculation);

        this.logger.info("Calculation is saved: " + calculation);

        return calculation;
    }
}
