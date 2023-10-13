package com.example.deneme.controller.impl;

import com.example.deneme.controller.ApiConfig;
import com.example.deneme.controller.ICalculationController;
import com.example.deneme.model.entity.Calculation;
import com.example.deneme.model.request.CreateCalculationRequest;
import com.example.deneme.service.WorkflowConfig;
import com.example.deneme.service.workflow.ICalculatorWorkflow;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.function.Supplier;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiConfig.CALCULATION_API)
public class CalculationController implements ICalculationController {

    private final Logger logger = LoggerFactory.getLogger(CalculationController.class);

    private final Supplier<ICalculatorWorkflow> calculatorWorkflow =
            () -> WorkflowConfig.getClient()
                    .newWorkflowStub(ICalculatorWorkflow.class, WorkflowConfig.Options.Workflow.CALCULATOR);

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Calculation> create(@RequestBody CreateCalculationRequest request) {
        this.logger.info("Create calculation request is received.");

        this.logger.debug("Calculation request: " + request);

        // WorkflowExecution we =  WorkflowClient.start(this.calculatorWorkflow::process,request);


        Calculation calculation = this.calculatorWorkflow.get().process(request);

        return ResponseEntity.status(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(calculation);
    }
}
