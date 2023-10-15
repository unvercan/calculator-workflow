package tr.unvercanunlu.calculator_workflow.service.activity.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.unvercanunlu.calculator_workflow.model.entity.Operand;
import tr.unvercanunlu.calculator_workflow.model.request.CreateOperandRequest;
import tr.unvercanunlu.calculator_workflow.repository.IOperandRepository;
import tr.unvercanunlu.calculator_workflow.service.activity.IOperandActivity;

@Service
@RequiredArgsConstructor
public class OperandActivity implements IOperandActivity {

    private final Logger logger = LoggerFactory.getLogger(OperandActivity.class);

    private final IOperandRepository operandRepository;

    @Override
    @Transactional
    public Operand create(CreateOperandRequest request) {
        Operand operand = Operand.builder()
                .first(request.getFirst())
                .second(request.getSecond())
                .calculationId(request.getCalculationId())
                .build();

        this.logger.info("Operand is created.");

        this.logger.debug("Created Operand: " + operand);

        operand = this.operandRepository.save(operand);

        this.logger.info("Created Operand is saved into the database.");

        this.logger.debug("Saved Operand: " + operand);

        return operand;
    }
}
