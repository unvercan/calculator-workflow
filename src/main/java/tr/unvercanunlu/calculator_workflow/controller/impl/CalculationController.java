package tr.unvercanunlu.calculator_workflow.controller.impl;

import io.temporal.api.common.v1.WorkflowExecution;
import io.temporal.client.WorkflowClient;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
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
import tr.unvercanunlu.calculator_workflow.service.workflow.IAsyncCalculatorWorkflow;
import tr.unvercanunlu.calculator_workflow.service.workflow.ICalculatorWorkflow;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

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

        this.logger.info("Process in Calculator Workflow is starting.");

        WorkflowExecution execution = WorkflowClient.start(calculatorWorkflow::process, request);
        this.logger.info("Execution of Process in Calculation Workflow with "
                + execution.getWorkflowId() + " Workflow ID and "
                + execution.getRunId() + " Run ID is starting.");

        Calculation calculation = calculatorWorkflow.process(request);

        this.logger.info("Execution of Process in Calculation Workflow is done.");

        CalculationDto calculationDto = CalculationDto.builder()
                .id(calculation.getId())
                .first(calculation.getOperand().getFirst())
                .second(calculation.getOperand().getSecond())
                .result(calculation.getResult().getValue())
                .operation(calculation.getOperation())
                .done(calculation.getDone())
                .build();

        this.logger.info("Calculation is created.");

        this.logger.debug("Created Calculation: " + calculationDto);

        return ResponseEntity.status(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(calculationDto);
    }

    @SneakyThrows
    @Override
    @RequestMapping(path = "/async", method = RequestMethod.POST)
    public ResponseEntity<CalculationDto> createAsync(@RequestBody CreateCalculationRequest request) {
        this.logger.info("Create Calculation Request is received.");

        this.logger.debug("Received Create Calculation Request: " + request);

        IAsyncCalculatorWorkflow asyncCalculatorWorkflow = WorkflowConfig.getAsyncCalculatorWorkflow();

        this.logger.info("Async Calculator Workflow is created.");

        this.logger.info("Async Process in Async Calculator Workflow is starting.");

        WorkflowExecution execution = WorkflowClient.start(asyncCalculatorWorkflow::processAsync, request);
        this.logger.info("Execution of Async Process in Async Calculation Workflow with "
                + execution.getWorkflowId() + " Workflow ID and "
                + execution.getRunId() + " Run ID is starting.");

        CompletableFuture<Calculation> calculationFuture = WorkflowClient.execute(asyncCalculatorWorkflow::processAsync, request);

        while (!calculationFuture.isDone()) {
            Thread.sleep(Duration.ofMillis(200).toMillis());

            this.logger.info("waiting for execution of Async Process in Async Calculation Workflow.");
        }

        this.logger.info("Execution of Async Process in Async Calculation Workflow is done.");

        Calculation calculation = calculationFuture.get();

        CalculationDto calculationDto = CalculationDto.builder()
                .id(calculation.getId())
                .first(calculation.getOperand().getFirst())
                .second(calculation.getOperand().getSecond())
                .result(calculation.getResult().getValue())
                .operation(calculation.getOperation())
                .done(calculation.getDone())
                .build();

        this.logger.info("Calculation is created.");

        this.logger.debug("Created Calculation: " + calculationDto);

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
