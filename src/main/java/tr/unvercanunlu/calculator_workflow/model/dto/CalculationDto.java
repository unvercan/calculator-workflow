package tr.unvercanunlu.calculator_workflow.model.dto;

import lombok.*;
import tr.unvercanunlu.calculator_workflow.model.entity.Operation;

import java.util.UUID;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CalculationDto {

    private UUID id;

    private Boolean done;

    private Operation operation;

    private Integer first;

    private Integer second;

    private Double result;

}
