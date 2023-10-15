package tr.unvercanunlu.calculator_workflow.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;

@ToString
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "operation")
@Table(name = "operation")
public class Operation implements Serializable {

    @Id
    @Column(name = "code")
    private Integer code;

    @Column(name = "name")
    private String name;

}
