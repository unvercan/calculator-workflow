package tr.unvercanunlu.calculator_workflow.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import tr.unvercanunlu.calculator_workflow.model.dto.CalculationDto;
import tr.unvercanunlu.calculator_workflow.model.request.CreateCalculationRequest;

@Validated
public interface ICalculationController {

    ResponseEntity<CalculationDto> create(
            @Valid
            @NotNull(message = "Calculation request should not be null.")
            CreateCalculationRequest request);

}
