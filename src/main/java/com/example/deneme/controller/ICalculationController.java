package com.example.deneme.controller;

import com.example.deneme.model.entity.Calculation;
import com.example.deneme.model.request.CreateCalculationRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

@Validated
public interface ICalculationController {

    ResponseEntity<Calculation> create(
            @NotNull(message = "Calculation request should not be null.")
            @Valid
            CreateCalculationRequest request);

}
