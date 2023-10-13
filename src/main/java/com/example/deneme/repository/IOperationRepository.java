package com.example.deneme.repository;

import com.example.deneme.model.entity.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IOperationRepository extends JpaRepository<Operation, Integer> {
}
