package tr.unvercanunlu.calculator_workflow.service.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import tr.unvercanunlu.calculator_workflow.model.entity.Calculation;
import tr.unvercanunlu.calculator_workflow.model.request.CreateCalculationRequest;

import java.util.UUID;

@Validated
@ActivityInterface
public interface ICalculationActivity {

    @ActivityMethod
    Calculation create(
            @Valid
            @NotNull(message = "Create Calculation Request should not be null.")
            CreateCalculationRequest request);

    @ActivityMethod
    void setOperand(
            @NotNull(message = "Calculation ID should not be null.") UUID calculationId,
            @NotNull(message = "Operand ID should not be null.") UUID operandId);

    @ActivityMethod
    void setResult(
            @NotNull(message = "Calculation ID should not be null.") UUID calculationId,
            @NotNull(message = "Result ID should not be null.") UUID resultId);

    @ActivityMethod
    void setDone(
            @NotNull(message = "Calculation ID should not be null.") UUID calculationId,
            @NotNull(message = "Completeness value of Calculation should not be null.") Boolean value);

}
