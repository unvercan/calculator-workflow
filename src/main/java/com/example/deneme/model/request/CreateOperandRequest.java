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
public class CreateOperandRequest implements Serializable {

    private Integer first;

    private Integer second;

    private UUID calculationId;

}
