package com.example.Recommendation_system.model;

import com.example.Recommendation_system.repository.RuleStatisticRepository;
import jakarta.persistence.*;
import lombok.Data;


import java.util.UUID;

@Entity
@Table(name = "rule_statistics")
@Data
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
}
