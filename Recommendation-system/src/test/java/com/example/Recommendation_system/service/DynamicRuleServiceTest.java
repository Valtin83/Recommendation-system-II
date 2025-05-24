package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.DynamicRule;
import com.example.Recommendation_system.model.RuleStatistic;
import com.example.Recommendation_system.repository.DynamicRuleRepository;
import com.example.Recommendation_system.repository.RuleStatisticRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class DynamicRuleServiceTest {

    @Mock
    private DynamicRuleRepository repository;

    @Mock
    private RuleStatisticRepository statisticRepository;

    @InjectMocks
    private DynamicRuleService dynamicRuleService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRule() {
        DynamicRule rule = new DynamicRule();
        rule.setId(UUID.randomUUID());
        rule.setConditions(new ArrayList<>());

        when(repository.save(any(DynamicRule.class))).thenReturn(rule);

        DynamicRule savedRule = dynamicRuleService.createRule(rule);

        assertNotNull(savedRule);
        assertEquals(rule.getId(), savedRule.getId());
        verify(statisticRepository).save(any(RuleStatistic.class));  // Ensure a statistic was saved
    }

    @Test
    public void testGetAllRules() {
        List<DynamicRule> rules = new ArrayList<>();
        rules.add(new DynamicRule());

        when(repository.findAll()).thenReturn(rules);

        List<DynamicRule> retrievedRules = dynamicRuleService.getAllRules();

        assertEquals(1, retrievedRules.size());
        verify(repository).findAll();  // Ensure findAll was called
    }

    @Test
    public void testDeleteRuleSuccess() {
        UUID ruleId = UUID.randomUUID();

        when(repository.existsById(ruleId)).thenReturn(true);

        boolean result = dynamicRuleService.deleteRule(ruleId);

        assertTrue(result);
        verify(statisticRepository).deleteByRuleId(ruleId);  // Ensure stats were deleted
        verify(repository).deleteById(ruleId);  // Ensure the rule was deleted
    }

    @Test
    public void testDeleteRuleNotFound() {
        UUID ruleId = UUID.randomUUID();

        when(repository.existsById(ruleId)).thenReturn(false);

        boolean result = dynamicRuleService.deleteRule(ruleId);

        assertFalse(result);
        verify(statisticRepository, never()).deleteByRuleId(any());  // Ensure stats were not deleted
        verify(repository, never()).deleteById(any());  // Ensure the rule was not deleted
    }

    @Test
    public void testIncrementRuleCounter() {
        UUID ruleId = UUID.randomUUID();
        RuleStatistic statistic = new RuleStatistic();
        statistic.setCount(0);

        when(statisticRepository.findByRuleId(ruleId)).thenReturn(statistic);

        dynamicRuleService.incrementRuleCounter(ruleId);

        assertEquals(1, statistic.getCount());
        verify(statisticRepository).save(statistic);  // Ensure the statistic was saved
    }

    @Test
    public void testIncrementRuleCounterNotFound() {
        UUID ruleId = UUID.randomUUID();

        when(statisticRepository.findByRuleId(ruleId)).thenReturn(null);

        dynamicRuleService.incrementRuleCounter(ruleId);

        verify(statisticRepository, never()).save(any());  // Ensure save was not called
    }
}
