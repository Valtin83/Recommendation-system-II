package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.DynamicRule;
import com.example.Recommendation_system.model.RuleStatistic;
import com.example.Recommendation_system.repository.DynamicRuleRepository;
import com.example.Recommendation_system.repository.RuleStatisticRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DynamicRuleService {
    private final DynamicRuleRepository repository;
    private final RuleStatisticRepository statisticRepository;

    public DynamicRuleService(DynamicRuleRepository repository, RuleStatisticRepository statisticRepository) {
        this.repository = repository;
        this.statisticRepository = statisticRepository;
    }

    public DynamicRule createRule(DynamicRule rule) {
        if (rule == null) {
            throw new IllegalArgumentException("Значение правила не может быть равным нулю");
        }
        if (rule.getConditions() != null && !rule.getConditions().isEmpty()) {
            rule.getConditions().forEach(condition -> condition.setDynamicRule(rule));
        }
        DynamicRule savedRule = repository.save(rule);

        RuleStatistic statistic = new RuleStatistic();
        statistic.setRuleId(savedRule.getId());
        statisticRepository.save(statistic);

        return savedRule;
    }


    public List<DynamicRule> getAllRules() {
        return repository.findAll();
    }

    public boolean deleteRule(UUID id) {
        if (repository.existsById(id)) {
            // Удаляем статистику при удалении правила
            statisticRepository.deleteByRuleId(id);
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    public void incrementRuleCounter(UUID ruleId) {
        RuleStatistic statistic = statisticRepository.findByRuleId(ruleId);
        if (statistic != null) {
            statistic.setCount(statistic.getCount() + 1);
            statisticRepository.save(statistic);
        }
    }
}