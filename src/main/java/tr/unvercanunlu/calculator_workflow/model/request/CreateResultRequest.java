package tr.unvercanunlu.calculator_workflow.model.request;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateResultRequest implements Serializable {

    @NotNull(message = "Operation Code should not be null.")
    @Min(value = 0, message = "Operation Code should be at least zero.")
    @Max(value = 8, message = "Operation Code should be at most eight.")
    @Digits(integer = 1, fraction = 0, message = "Operation Code should be integer and has at most one digit.")
    private Integer operationCode;

    @NotNull(message = "Operand ID should not be null.")
    private UUID operandId;

    @NotNull(message = "Calculation ID should not be null.")
    private UUID calculationId;

}
