package com.example.deneme.model.request;

import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCalculationRequest implements Serializable {

    private Integer first;

    private Integer second;

    private Integer operationCode;

}
