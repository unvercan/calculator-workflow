package tr.unvercanunlu.calculator_workflow.service.activity.impl;

import io.temporal.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.unvercanunlu.calculator_workflow.model.entity.Operand;
import tr.unvercanunlu.calculator_workflow.model.request.CreateOperandRequest;
import tr.unvercanunlu.calculator_workflow.repository.IOperandRepository;
import tr.unvercanunlu.calculator_workflow.service.activity.IOperandActivity;

@Service
@RequiredArgsConstructor
public class OperandActivity implements IOperandActivity {

    private final Logger workflowLogger = Workflow.getLogger(OperandActivity.class);

    private final IOperandRepository operandRepository;

    @Override
    @Transactional
    public Operand create(CreateOperandRequest request) {
        this.workflowLogger.info("Create in Operand Activity is started.");

        Operand operand = Operand.builder()
                .first(request.getFirst())
                .second(request.getSecond())
                .calculationId(request.getCalculationId())
                .build();

        this.workflowLogger.info("Operand is created.");

        this.workflowLogger.debug("Created Operand: " + operand);

        operand = this.operandRepository.save(operand);

        this.workflowLogger.info("Created Operand is saved into the database.");

        this.workflowLogger.debug("Saved Operand: " + operand);

        this.workflowLogger.info("Create in Operand Activity is completed.");

        return operand;
    }
}
