package com.example.deneme.repository;

import com.example.deneme.model.entity.Operand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface IOperandRepository extends JpaRepository<Operand, UUID> {
}
