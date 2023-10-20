package tr.unvercanunlu.calculator_workflow.service.activity.impl;

import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.unvercanunlu.calculator_workflow.model.entity.Operand;
import tr.unvercanunlu.calculator_workflow.model.entity.Operation;
import tr.unvercanunlu.calculator_workflow.model.entity.Result;
import tr.unvercanunlu.calculator_workflow.model.request.CreateResultRequest;
import tr.unvercanunlu.calculator_workflow.repository.IOperandRepository;
import tr.unvercanunlu.calculator_workflow.repository.IOperationRepository;
import tr.unvercanunlu.calculator_workflow.repository.IResultRepository;
import tr.unvercanunlu.calculator_workflow.service.activity.IResultActivity;

@Service
@RequiredArgsConstructor
public class ResultActivity implements IResultActivity {

    private final Logger workflowLogger = Workflow.getLogger(ResultActivity.class);

    private final IOperandRepository operandRepository;

    private final IOperationRepository operationRepository;

    private final IResultRepository resultRepository;

    @Override
    @Transactional
    public Result create(CreateResultRequest request) {
        this.workflowLogger.info("Create in Result Activity is started.");

        Operation operation = this.operationRepository.findById(request.getOperationCode())
                .orElseThrow(() -> new RuntimeException("Operation with " + request.getOperationCode() + " Code cannot be found."));

        this.workflowLogger.info("Operation with " + request.getOperationCode() + " Code is fetched from the database.");

        this.workflowLogger.debug("Fetched Operation: " + operation);

        Operand operand = this.operandRepository.findById(request.getOperandId())
                .orElseThrow(() -> new RuntimeException("Operand with " + request.getOperandId() + " ID cannot be found."));

        this.workflowLogger.info("Operand with " + request.getOperandId() + " ID is fetched from the database.");

        this.workflowLogger.debug("Fetched Operand: " + operand);

        Double resultValue = switch (operation.getCode()) {
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
                .value(resultValue)
                .calculationId(request.getCalculationId())
                .build();

        this.workflowLogger.info("Result is created.");

        this.workflowLogger.debug("Created Result: " + result);

        result = this.resultRepository.save(result);

        this.workflowLogger.info("Created Result is saved into the database.");

        this.workflowLogger.debug("Saved Result: " + result);

        this.workflowLogger.info("Create in Result Activity is completed.");

        return result;
    }
}
