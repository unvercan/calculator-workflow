package com.example.deneme.model.entity;

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
@Entity(name = "operand")
@Table(name = "operand")
public class Operand implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @Column(name = "first")
    private Integer first;

    @Column(name = "second")
    private Integer second;

    @Column(name = "calculation_id")
    private UUID calculationId;

    /*
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calculation_id")
    private Calculation calculation;
    */
}
