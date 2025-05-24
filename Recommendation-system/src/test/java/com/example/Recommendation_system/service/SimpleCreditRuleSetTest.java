package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.Recommendation;
import com.github.benmanes.caffeine.cache.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class SimpleCreditRuleSetTest {

    private JdbcTemplate jdbcTemplate;
    private Cache<String, Boolean> cache;
    private SimpleCreditRuleSet ruleSet;
    private UUID userId;

    @BeforeEach
    void setUp() {
        jdbcTemplate = mock(JdbcTemplate.class);
        cache = mock(Cache.class);
        ruleSet = new SimpleCreditRuleSet(jdbcTemplate, cache);
        userId = UUID.randomUUID();
    }

    @Test
    void testNoCreditProducts() {
        // Кэш возвращает null, имитируем запрос
        when(cache.getIfPresent(ArgumentMatchers.contains(userId + "_NO_CREDIT_PRODUCTS"))).thenReturn(null);
        when(jdbcTemplate.queryForObject(anyString(), eq(Boolean.class), eq(userId)))
                .thenReturn(true); // Нет кредитных продуктов

        // Кэш возвращает true для noCreditProducts
        when(cache.getIfPresent(ArgumentMatchers.contains(userId + "_NO_CREDIT_PRODUCTS"))).thenReturn(true);

        Optional<Object> result = ruleSet.getRecommendation(userId);

        assertTrue(result.isPresent());
        List<Recommendation> recs = (List<Recommendation>) result.get();
        assertEquals(1, recs.size());
        assertEquals("Простой кредит", recs.get(0));
        assertEquals("Откройте мир выгодных кредитов с нами! Ищете способ быстро и без лишних хлопот получить нужную сумму? Тогда наш выгодный кредит — именно то, что вам нужно", recs.get(0).getDescription());
    }

    @Test
    void testIncomeGreaterThanSpending() {
        // Кэш возвращает null, имитируем запрос
        when(cache.getIfPresent(ArgumentMatchers.contains(userId + "_NO_CREDIT_PRODUCTS"))).thenReturn(false);
        when(cache.getIfPresent(ArgumentMatchers.contains(userId + "_INCOME_GREATER_THAN_SPENDING"))).thenReturn(null);
        when(jdbcTemplate.queryForObject(anyString(), eq(Boolean.class), eq(userId)))
                .thenReturn(true); // Доход больше расходов

        // Кэш возвращает true для incomeGreater
        when(cache.getIfPresent(ArgumentMatchers.contains(userId + "_INCOME_GREATER_THAN_SPENDING"))).thenReturn(true);

        Optional<Object> result = ruleSet.getRecommendation(userId);

        assertTrue(result.isPresent());
        List<Recommendation> recs = (List<Recommendation>) result.get();
        assertEquals(1, recs.size());
        assertEquals("Простой кредит", recs.get(0));
    }

    @Test
    void testSpendingGreaterThan100K() {
        // Кэш возвращает null, имитируем запрос
        when(cache.getIfPresent(ArgumentMatchers.contains(userId + "_NO_CREDIT_PRODUCTS"))).thenReturn(false);
        when(cache.getIfPresent(ArgumentMatchers.contains(userId + "_INCOME_GREATER_THAN_SPENDING"))).thenReturn(false);
        when(cache.getIfPresent(ArgumentMatchers.contains(userId + "_SPENDING_GREATER_THAN_100K"))).thenReturn(null);
        when(jdbcTemplate.queryForObject(anyString(), eq(Boolean.class), eq(userId)))
                .thenReturn(true); // Расходы > 100000

        // Кэш возвращает true для spendingGreater
        when(cache.getIfPresent(ArgumentMatchers.contains(userId + "_SPENDING_GREATER_THAN_100K"))).thenReturn(true);

        Optional<Object> result = ruleSet.getRecommendation(userId);

        assertTrue(result.isPresent());
        List<Recommendation> recs = (List<Recommendation>) result.get();
        assertEquals(1, recs.size());
    }

    @Test
    void testNoConditionsMet() {
        // Все кэши возвращают null, имитируем запросы, возвращающие false
        when(cache.getIfPresent(anyString())).thenReturn(null);
        when(jdbcTemplate.queryForObject(anyString(), eq(Boolean.class), eq(userId)))
                .thenReturn(false);

        // Кэш возвращает false для всех условий
        when(cache.getIfPresent(anyString())).thenReturn(false);

        Optional<Object> result = ruleSet.getRecommendation(userId);

        assertTrue(result.isPresent());
        List<Recommendation> recs = (List<Recommendation>) result.get();
        assertEquals(1, recs.size());
        assertEquals("Простой кредит", recs.get(0));
        assertEquals("Нет рекомендации", recs.get(0).getDescription());
    }
}
