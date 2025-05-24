package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.Recommendation;
import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static javax.management.Query.eq;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public class Invest500RuleSetTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private Cache<String, Boolean> cache;

    @InjectMocks
    private Invest500RuleSet invest500RuleSet;

    private UUID userId;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        userId = UUID.randomUUID();
    }

    @Test
    public void testGetRecommendationDebitUsageTrue() {
        // Настраиваем кэш
        when(cache.getIfPresent(userId + "_INVEST_DEBIT_COUNT")).thenReturn(null);
        when(jdbcTemplate.queryForObject(anyString(), eq(Boolean.class), eq(userId))).thenReturn(true);

        // Устанавливаем поведение для других запросов
        when(cache.getIfPresent(userId + "_INVEST_PRODUCT_COUNT")).thenReturn(false);
        when(cache.getIfPresent(userId + "_SAVING_SUM_OVER_1000")).thenReturn(false);

        // Получаем рекомендацию
        Optional<Object> recommendation = invest500RuleSet.getRecommendation(userId);

        // Проверяем результат
        assertTrue(recommendation.isPresent());
        assertEquals(1, ((List<Recommendation>) recommendation.get()).size());
        assertEquals("Invest 500", ((List<Recommendation>) recommendation.get()).get(0));
        assertEquals(invest500RuleSet.DESCRIPTION, ((List<Recommendation>) recommendation.get()).get(0).getDescription());
    }

    @Test
    public void testGetRecommendationNoInvestProductsTrue() {
        // Настраиваем кэш
        when(cache.getIfPresent(userId + "_INVEST_DEBIT_COUNT")).thenReturn(false);
        when(cache.getIfPresent(userId + "_INVEST_PRODUCT_COUNT")).thenReturn(null);
        when(jdbcTemplate.queryForObject(anyString(), eq(Boolean.class), eq(userId))).thenReturn(true);

        // Устанавливаем поведение для других запросов
        when(cache.getIfPresent(userId + "_SAVING_SUM_OVER_1000")).thenReturn(false);

        // Получаем рекомендацию
        Optional<Object> recommendation = invest500RuleSet.getRecommendation(userId);

        // Проверяем результат
        assertTrue(recommendation.isPresent());
        assertEquals(1, ((List<Recommendation>) recommendation.get()).size());
        assertEquals("Invest 500", ((List<Recommendation>) recommendation.get()).get(0).getTitle());
        assertEquals(invest500RuleSet.DESCRIPTION, ((List<Recommendation>) recommendation.get()).get(0).getDescription());
    }

    @Test
    public void testGetRecommendationSavingOver1000True() {
        // Настраиваем кэш
        when(cache.getIfPresent(userId + "_INVEST_DEBIT_COUNT")).thenReturn(false);
        when(cache.getIfPresent(userId + "_INVEST_PRODUCT_COUNT")).thenReturn(false);
        when(cache.getIfPresent(userId + "_SAVING_SUM_OVER_1000")).thenReturn(null);
        when(jdbcTemplate.queryForObject(anyString(), eq(Boolean.class), eq(userId))).thenReturn(true);

        // Получаем рекомендацию
        Optional<Object> recommendation = invest500RuleSet.getRecommendation(userId);

        // Проверяем результат
        assertTrue(recommendation.isPresent());
        assertEquals(1, ((List<Recommendation>) recommendation.get()).size());
        assertEquals("Invest 500", ((List<Recommendation>) recommendation.get()).get(0));
        assertEquals(invest500RuleSet.DESCRIPTION, ((List<Recommendation>) recommendation.get()).get(0).getDescription());
    }

    @Test
    public void testGetRecommendationNoRecommendation() {
        // Настраиваем кэш
        when(cache.getIfPresent(userId + "_INVEST_DEBIT_COUNT")).thenReturn(false);
        when(cache.getIfPresent(userId + "_INVEST_PRODUCT_COUNT")).thenReturn(false);
        when(cache.getIfPresent(userId + "_SAVING_SUM_OVER_1000")).thenReturn(false);

        // Получаем рекомендацию
        Optional<Object> recommendation = invest500RuleSet.getRecommendation(userId);

        // Проверяем результат
        assertTrue(recommendation.isPresent());
        assertEquals(1, ((List<Recommendation>) recommendation.get()).size());
        assertEquals("Invest 500", ((List<Recommendation>) recommendation.get()).get(0).getTitle());
        assertEquals(invest500RuleSet.NO_RECOMMENDATION, ((List<Recommendation>) recommendation.get()).get(0).getDescription());
    }
}
