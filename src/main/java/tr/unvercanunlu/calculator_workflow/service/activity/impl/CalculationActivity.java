package tr.unvercanunlu.calculator_workflow.service.activity.impl;

import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
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

    private final Logger workflowLogger = Workflow.getLogger(CalculationActivity.class);

    private final ICalculationRepository calculationRepository;

    private final IOperationRepository operationRepository;

    @Override
    @Transactional
    public Calculation create(CreateCalculationRequest request) {
        this.workflowLogger.info("Create in Calculation Activity is started.");

        Operation operation = this.operationRepository.findById(request.getOperationCode())
                .orElseThrow(() -> new RuntimeException("Operation with " + request.getOperationCode() + " Code cannot be found."));

        this.workflowLogger.info("Operation with " + request.getOperationCode() + " Code is fetched from the database.");

        this.workflowLogger.debug("Fetched Operation: " + operation);

        Calculation calculation = Calculation.builder()
                .operation(operation)
                .done(Boolean.FALSE)
                .build();

        this.workflowLogger.info("Calculation is created.");

        this.workflowLogger.debug("Created Calculation: " + calculation);

        calculation = this.calculationRepository.save(calculation);

        this.workflowLogger.info("Created Calculation is saved into the database.");

        this.workflowLogger.debug("Saved Calculation: " + calculation);

        this.workflowLogger.info("Create in Calculation Activity is completed.");

        return calculation;
    }

    @Override
    @Transactional
    public void setOperand(UUID calculationId, UUID operandId) {
        this.workflowLogger.info("Set Operand in Calculation Activity is started.");

        this.calculationRepository.setOperand(calculationId, operandId);

        this.workflowLogger.info("Operand with " + operandId + " ID is set for Calculation with " + calculationId + " ID.");

        this.workflowLogger.info("Set Operand in Calculation Activity is completed.");
    }

    @Override
    @Transactional
    public void setResult(UUID calculationId, UUID resultId) {
        this.workflowLogger.info("Set Result in Calculation Activity is started.");

        this.calculationRepository.setResult(calculationId, resultId);

        this.workflowLogger.info("Result with " + resultId + " ID is set for Calculation with " + calculationId + " ID.");

        this.workflowLogger.info("Set Result in Calculation Activity is completed.");
    }

    @Override
    @Transactional
    public void setCompleteness(UUID calculationId, Boolean value) {
        this.workflowLogger.info("Set Completeness in Calculation Activity is started.");

        this.calculationRepository.setCompleteness(calculationId, value);

        this.workflowLogger.info("Completeness value of Calculation with " + calculationId + " ID is set " + value + ".");

        this.workflowLogger.info("Set Completeness in Calculation Activity is completed.");
    }
}
