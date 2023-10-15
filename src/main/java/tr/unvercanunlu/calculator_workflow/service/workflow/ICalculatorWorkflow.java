package tr.unvercanunlu.calculator_workflow.service.workflow;

import io.temporal.workflow.WorkflowInterface;
import io.temporal.workflow.WorkflowMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import tr.unvercanunlu.calculator_workflow.model.entity.Calculation;
import tr.unvercanunlu.calculator_workflow.model.request.CreateCalculationRequest;

@Validated
@WorkflowInterface
public interface ICalculatorWorkflow {

    @WorkflowMethod
    Calculation process(
            @Valid
            @NotNull(message = "Create Calculation Request should not be null.")
            CreateCalculationRequest request);

}
