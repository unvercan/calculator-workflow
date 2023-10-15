package tr.unvercanunlu.calculator_workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tr.unvercanunlu.calculator_workflow.model.entity.Result;

import java.util.UUID;

@Repository
public interface IResultRepository extends JpaRepository<Result, UUID> {
}
