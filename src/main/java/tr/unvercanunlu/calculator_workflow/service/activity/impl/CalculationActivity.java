package tr.unvercanunlu.calculator_workflow.service.activity.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.unvercanunlu.calculator_workflow.model.entity.Calculation;
import tr.unvercanunlu.calculator_workflow.model.entity.Operation;
import tr.unvercanunlu.calculator_workflow.model.request.CreateCalculationRequest;
import tr.unvercanunlu.calculator_workflow.repository.ICalculationRepository;
import tr.unvercanunlu.calculator_workflow.repository.IOperationRepository;
import tr.unvercanunlu.calculator_workflow.service.activity.ICalculationActivity;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CalculationActivity implements ICalculationActivity {

    private final Logger logger = LoggerFactory.getLogger(CalculationActivity.class);

    private final ICalculationRepository calculationRepository;

    private final IOperationRepository operationRepository;

    @Override
    @Transactional
    public Calculation create(CreateCalculationRequest request) {
        Operation operation = this.operationRepository.findById(request.getOperationCode())
                .orElseThrow(() -> new RuntimeException("Operation with " + request.getOperationCode() + " Code cannot be found."));

        this.logger.info("Operation with " + request.getOperationCode() + " Code is fetched from the database.");

        this.logger.debug("Fetched Operation: " + operation);

        Calculation calculation = Calculation.builder()
                .operation(operation)
                .done(Boolean.FALSE)
                .build();

        this.logger.info("Calculation is created.");

        this.logger.debug("Created Calculation: " + calculation);

        calculation = this.calculationRepository.save(calculation);

        this.logger.info("Created Calculation is saved into the database.");

        this.logger.debug("Saved Calculation: " + calculation);

        return calculation;
    }

    @Override
    @Transactional
    public void setOperand(UUID calculationId, UUID operandId) {
        this.calculationRepository.setOperand(calculationId, operandId);

        this.logger.info("Operand with " + operandId + " ID is set for Calculation with " + calculationId + " ID.");
    }

    @Override
    @Transactional
    public void setResult(UUID calculationId, UUID resultId) {
        this.calculationRepository.setResult(calculationId, resultId);

        this.logger.info("Result with " + resultId + " ID is set for Calculation with " + calculationId + " ID.");
    }

    @Override
    @Transactional
    public void setDone(UUID calculationId, Boolean value) {
        this.calculationRepository.setDone(calculationId, value);

        this.logger.info("Completeness value of Calculation with " + calculationId + " ID is set " + value + ".");
    }
}
