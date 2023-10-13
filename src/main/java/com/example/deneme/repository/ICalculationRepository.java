package com.example.deneme.repository;

import com.example.deneme.model.entity.Calculation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ICalculationRepository extends JpaRepository<Calculation, UUID> {
}
