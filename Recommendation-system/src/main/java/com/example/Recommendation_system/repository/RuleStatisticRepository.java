package com.example.Recommendation_system.repository;

import com.example.Recommendation_system.model.RuleStatistic;
import jakarta.transaction.Transactional;
import org.springframework.data.jdbc.repository.query.Modifying;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RuleStatisticRepository extends JpaRepository<RuleStatistic, UUID> {
    RuleStatistic findByRuleId(UUID ruleId);

    @Transactional
    @Modifying
    @Query("DELETE FROM RuleStatistic rs WHERE rs.ruleId = :ruleId")
    void deleteByRuleId(UUID ruleId);
}