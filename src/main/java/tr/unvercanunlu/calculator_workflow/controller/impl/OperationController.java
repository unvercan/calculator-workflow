package tr.unvercanunlu.calculator_workflow.controller.impl;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import tr.unvercanunlu.calculator_workflow.controller.ApiConfig;
import tr.unvercanunlu.calculator_workflow.controller.IOperationController;
import tr.unvercanunlu.calculator_workflow.model.entity.Operation;
import tr.unvercanunlu.calculator_workflow.repository.IOperationRepository;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = ApiConfig.OPERATION_API)
public class OperationController implements IOperationController {

    private final Logger logger = LoggerFactory.getLogger(OperationController.class);

    private final IOperationRepository operationRepository;

    @Override
    @RequestMapping(method = RequestMethod.GET)
    public ResponseEntity<List<Operation>> retrieveAll() {
        List<Operation> operationList = this.operationRepository.findAll();

        this.logger.info("All Operations are fetched from the database.");

        this.logger.debug("Fetched Operations: " + operationList);

        return ResponseEntity.status(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(operationList);
    }

    @Override
    @RequestMapping(path = "/{operationCode}", method = RequestMethod.GET)
    public ResponseEntity<Operation> retrieve(@PathVariable(name = "operationCode") Integer operationCode) {
        Operation operation = this.operationRepository.findById(operationCode)
                .orElseThrow(() -> new RuntimeException("Operation with " + operationCode + " Code cannot be found."));

        this.logger.info("Operation with " + operationCode + " Code is fetched from the database.");

        this.logger.debug("Fetched Operation: " + operation);

        return ResponseEntity.status(HttpStatus.OK.value())
                .contentType(MediaType.APPLICATION_JSON)
                .body(operation);
    }
}
