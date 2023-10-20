package tr.unvercanunlu.calculator_workflow.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tr.unvercanunlu.calculator_workflow.model.entity.Calculation;

import java.util.UUID;

@Repository
public interface ICalculationRepository extends JpaRepository<Calculation, UUID> {

    @Modifying
    @Query(value = "UPDATE \"calculation\" " +
            "SET \"calculation\".\"operand_id\" = :operandId " +
            "WHERE \"calculation\".\"id\" = :calculationId", nativeQuery = true)
    void setOperand(
            @Param(value = "calculationId") UUID calculationId,
            @Param(value = "operandId") UUID operandId);

    @Modifying
    @Query(value = "UPDATE \"calculation\" " +
            "SET \"calculation\".\"result_id\" = :resultId " +
            "WHERE \"calculation\".\"id\" = :calculationId", nativeQuery = true)
    void setResult(
            @Param(value = "calculationId") UUID calculationId,
            @Param(value = "resultId") UUID resultId);

    @Modifying
    @Query(value = "UPDATE \"calculation\" " +
            "SET \"calculation\".\"done\" = :done " +
            "WHERE \"calculation\".\"id\" = :calculationId", nativeQuery = true)
    void setCompleteness(
            @Param(value = "calculationId") UUID calculationId,
            @Param(value = "done") Boolean done);

}
