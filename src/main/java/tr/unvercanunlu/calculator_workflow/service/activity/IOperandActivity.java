package tr.unvercanunlu.calculator_workflow.service.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import tr.unvercanunlu.calculator_workflow.model.entity.Operand;
import tr.unvercanunlu.calculator_workflow.model.request.CreateOperandRequest;

@Validated
@ActivityInterface
public interface IOperandActivity {

    @ActivityMethod
    Operand create(
            @Valid
            @NotNull(message = "Create Operand Request should not be null.")
            CreateOperandRequest request);

}
