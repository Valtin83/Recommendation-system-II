package com.example.Recommendation_system.repository;

import com.example.Recommendation_system.model.DynamicRule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface DynamicRuleRepository extends JpaRepository<DynamicRule, UUID> {
    boolean existsById(UUID id);

    void deleteById(UUID id);
}