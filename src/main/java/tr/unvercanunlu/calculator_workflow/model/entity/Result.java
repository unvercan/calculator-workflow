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
@Entity(name = "result")
@Table(name = "result")
public class Result implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "value")
    private Double value;

    @Column(name = "calculation_id")
    private UUID calculationId;

}
