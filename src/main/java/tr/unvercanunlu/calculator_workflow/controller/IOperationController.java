package tr.unvercanunlu.calculator_workflow.controller;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import tr.unvercanunlu.calculator_workflow.model.entity.Operation;

import java.util.List;

@Validated
public interface IOperationController {

    ResponseEntity<List<Operation>> retrieveAll();

    ResponseEntity<Operation> retrieve(
            @NotNull(message = "Operation Code should not be null.")
            @Min(value = 0, message = "Operation Code should be at least zero.")
            @Max(value = 8, message = "Operation Code should be at most eight.")
            @Digits(integer = 1, fraction = 0, message = "Operation Code should be integer and has at most one digit.")
            Integer operationCode);

}
