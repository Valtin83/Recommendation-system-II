package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.DynamicRule;
import com.example.Recommendation_system.repository.DynamicRuleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class DynamicRuleService {
    private final DynamicRuleRepository repository;

    public DynamicRuleService(DynamicRuleRepository repository) {
        this.repository = repository;
    }

    public DynamicRule createRule(DynamicRule rule) {
        // связь RuleCondition и DynamicRule
        if (rule.getConditions() != null) {
            rule.getConditions().forEach(condition -> condition.setDynamicRule(rule));
        }
        return repository.save(rule);
    }

    public List<DynamicRule> getAllRules() {
        return repository.findAll();
    }

    public boolean deleteRule(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }
}
