package tr.unvercanunlu.calculator_workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tr.unvercanunlu.calculator_workflow.model.entity.Operation;

@Repository
public interface IOperationRepository extends JpaRepository<Operation, Integer> {
}
