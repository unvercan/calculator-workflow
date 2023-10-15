package tr.unvercanunlu.calculator_workflow.model.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "calculation")
@Table(name = "calculation")
public class Calculation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "done")
    private Boolean done;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "operation_code")
    private Operation operation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "operand_id")
    private Operand operand;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "result_id")
    private Result result;

}
