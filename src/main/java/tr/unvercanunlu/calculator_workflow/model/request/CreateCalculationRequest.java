package tr.unvercanunlu.calculator_workflow.model.request;

import jakarta.validation.constraints.*;
import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCalculationRequest implements Serializable {

    @Positive(message = "First Operand should be positive integer.")
    @Digits(integer = 2, fraction = 0, message = "First Operand should be integer.")
    @NotNull(message = "First Operand cannot be null.")
    @Min(value = 1, message = "First Operand should be at least one.")
    @Max(value = 10, message = "First Operand should be at most ten.")
    private Integer first;

    @Positive(message = "Second Operand should be positive integer.")
    @Digits(integer = 2, fraction = 0, message = "Second Operand should be integer.")
    @NotNull(message = "Second Operand cannot be null.")
    @Min(value = 1, message = "Second Operand should be at least one.")
    @Max(value = 10, message = "Second Operand should be at most ten.")
    private Integer second;

    @NotNull(message = "Operation Code should not be null.")
    @Min(value = 0, message = "Operation Code should be at least zero.")
    @Max(value = 8, message = "Operation Code should be at most eight.")
    @Digits(integer = 1, fraction = 0, message = "Operation Code should be integer and has at most one digit.")
    private Integer operationCode;

}
