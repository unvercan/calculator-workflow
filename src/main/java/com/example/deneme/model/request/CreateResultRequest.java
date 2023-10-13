package com.example.deneme.model.request;

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

    private UUID operandId;

    private Integer operationCode;

    private UUID calculationId;

}
