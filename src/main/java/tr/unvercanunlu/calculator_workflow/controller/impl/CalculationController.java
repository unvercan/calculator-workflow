package tr.unvercanunlu.calculator_workflow.controller.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tr.unvercanunlu.calculator_workflow.controller.ApiConfig;
import tr.unvercanunlu.calculator_workflow.controller.ICalculationController;
import tr.unvercanunlu.calculator_workflow.model.dto.CalculationDto;
import tr.unvercanunlu.calculator_workflow.model.entity.Calculation;
import tr.unvercanunlu.calculator_workflow.model.request.CreateCalculationRequest;
import tr.unvercanunlu.calculator_workflow.repository.ICalculationRepository;
import tr.unvercanunlu.calculator_workflow.service.WorkflowConfig;
import tr.unvercanunlu.calculator_workflow.service.workflow.ICalculatorWorkflow;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiConfig.CALCULATION_API)
public class CalculationController implements ICalculationController {

    private final Logger logger = LoggerFactory.getLogger(CalculationController.class);

    private final ICalculationRepository calculationRepository;

    @Override
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<CalculationDto> create(@RequestBody CreateCalculationRequest request) {
        this.logger.info("Create Calculation Request is received.");

        this.logger.debug("Received Create Calculation Request: " + request);

        ICalculatorWorkflow calculatorWorkflow = WorkflowConfig.getCalculatorWorkflow();

        this.logger.info("Calculator Workflow is created.");

        this.logger.info("Calculation Process in Calculator Workflow is starting.");

        Calculation calculation = calculatorWorkflow.process(request);

        CalculationDto calculationDto = CalculationDto.builder()
                .id(calculation.getId())
                .first(calculation.getOperand().getFirst())
                .second(calculation.getOperand().getSecond())
                .result(calculation.getResult().getValue())
                .operation(calculation.getOperation())
                .done(calculation.getDone())
                .build();

        this.logger.info("Calculation Result is created.");

        this.logger.debug("Created Calculation Result: " + calculationDto);

        return ResponseEntity.status(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(calculationDto);
    }

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<CalculationDto>> retrieveAll() {
        List<Calculation> calculationList = this.calculationRepository.findAll();

        this.logger.info("All Calculations are fetched from the database.");

        this.logger.debug("Fetched Calculations: " + calculationList);

        List<CalculationDto> calculationDtoList = calculationList.stream()
                .map(calculation -> CalculationDto.builder()
                        .id(calculation.getId())
                        .first(calculation.getOperand().getFirst())
                        .second(calculation.getOperand().getSecond())
                        .result(calculation.getResult().getValue())
                        .operation(calculation.getOperation())
                        .done(calculation.getDone())
                        .build())
                .toList();

        return ResponseEntity.status(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(calculationDtoList);
    }

    @Override
    @RequestMapping(path = "/{calculationId}", method = RequestMethod.GET)
    public ResponseEntity<CalculationDto> retrieve(@PathVariable(name = "calculationId") UUID calculationId) {
        Calculation calculation = this.calculationRepository.findById(calculationId)
                .orElseThrow(() -> new RuntimeException("Calculation with " + calculationId + " ID cannot be found."));

        this.logger.info("Calculation with " + calculationId + " ID is fetched from the database.");

        this.logger.debug("Fetched Calculation: " + calculation);

        CalculationDto calculationDto = CalculationDto.builder()
                .id(calculation.getId())
                .first(calculation.getOperand().getFirst())
                .second(calculation.getOperand().getSecond())
                .result(calculation.getResult().getValue())
                .operation(calculation.getOperation())
                .done(calculation.getDone())
                .build();

        return ResponseEntity.status(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(calculationDto);
    }
}
