package com.example.Recommendation_system.repository;

import com.example.Recommendation_system.model.Rules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RulesRepository extends JpaRepository<Rules, UUID> {
    @Override
    Optional<Rules> findById(UUID uuid);
}
