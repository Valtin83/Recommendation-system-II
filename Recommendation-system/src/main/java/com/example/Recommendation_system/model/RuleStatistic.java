package com.example.Recommendation_system.model;

import com.example.Recommendation_system.repository.RuleStatisticRepository;
import jakarta.persistence.*;
import lombok.Data;


import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "rule_statistics")

public class RuleStatistic {        //Сбор статистики которые срабатывают правила
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(name = "rule_id", nullable = false)
    private UUID ruleId;

    @Column(name = "count", nullable = false)
    private int count = 0;


    public Class<RuleStatisticRepository> RuleStatistic(String key, Integer value) {
        return RuleStatisticRepository.class;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getRuleId() {
        return ruleId;
    }

    public void setRuleId(UUID ruleId) {
        this.ruleId = ruleId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RuleStatistic that = (RuleStatistic) o;
        return count == that.count && Objects.equals(id, that.id) && Objects.equals(ruleId, that.ruleId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ruleId, count);
    }

    @Override
    public String toString() {
        return "RuleStatistic{" +
                "id=" + id +
                ", ruleId=" + ruleId +
                ", count=" + count +
                '}';
    }
}
