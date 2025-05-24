package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.Recommendation;
import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

class TopSavingRuleSetTest {
    private JdbcTemplate jdbcTemplate;
    private Cache<String, Boolean> cache;
    private TopSavingRuleSet topSavingRuleSet;

    @BeforeEach
    public void setUp() {
        jdbcTemplate = Mockito.mock(JdbcTemplate.class);
        cache = Mockito.mock(Cache.class);
        topSavingRuleSet = new TopSavingRuleSet(jdbcTemplate, cache);
    }

    @Test
    public void testGetRecommendation_NoDebitTransactions_NoRecommendation() {
        // Mock cache and jdbcTemplate responses
        Mockito.when(cache.getIfPresent(any())).thenReturn(null);
        Mockito.when(jdbcTemplate.queryForObject(any(String.class), any(Object[].class), any(Class.class)))
                .thenReturn(false);

        // Act
        Optional<Object> result = topSavingRuleSet.getRecommendation(UUID.randomUUID());

        // Assert
        assertEquals("No recommendation", ((List<Recommendation>) result.get()).get(0));
    }

    @Test
    public void testGetRecommendation_HasDebitAndIncomeAboveThreshold_RecommendationProvided() {
        // Mocking responses for this specific case
        Mockito.when(cache.getIfPresent(any())).thenReturn(null);
        Mockito.when(cache.getIfPresent("...")).thenReturn(true); // Assume has debit
        Mockito.when(jdbcTemplate.queryForObject(any(String.class), any(Object[].class), any(Class.class)))
                .thenReturn(true); // Assume income condition met

        // Act
        Optional<Object> result = topSavingRuleSet.getRecommendation(UUID.randomUUID());

        // Assert
        assertEquals("Top saving", ((List<Recommendation>) result.get()).get(0));
    }
}
