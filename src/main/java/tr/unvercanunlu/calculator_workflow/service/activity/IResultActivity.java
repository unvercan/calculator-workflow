package tr.unvercanunlu.calculator_workflow.service.activity;

import io.temporal.activity.ActivityInterface;
import io.temporal.activity.ActivityMethod;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;
import tr.unvercanunlu.calculator_workflow.model.entity.Result;
import tr.unvercanunlu.calculator_workflow.model.request.CreateResultRequest;

@Validated
@ActivityInterface
public interface IResultActivity {

    @ActivityMethod
    Result create(
            @Valid
            @NotNull(message = "Create Result Request should not be null.")
            CreateResultRequest request);

}
