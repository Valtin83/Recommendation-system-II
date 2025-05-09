package com.example.Recommendation_system.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.example.Recommendation_system.model.RecommendationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.Optional;

public class TopSavingRuleSetTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private TopSavingRuleSet topSavingRuleSet;

    private static final String USER_ID = "test-user-id";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRecommendationWithNoDebitProducts() {
        // Имитация отсутствия дебетовых продуктов
        when(jdbcTemplate.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(Collections.emptyList());

        // Проверяем, что рекомендация не возвращается
        Optional<RecommendationDTO> recommendation = topSavingRuleSet.getRecommendation(USER_ID, jdbcTemplate);
        assertTrue(recommendation.isEmpty());
    }

    @Test
    public void testGetRecommendationWithInvestProducts() {
        // Имитация наличия дебетовых продуктов
        when(jdbcTemplate.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(Collections.singletonList(Collections.singletonMap("1", 1)));

        // Имитация наличия инвестиционных продуктов
        when(jdbcTemplate.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(Collections.singletonList(Collections.singletonMap("1", 1)));

        // Проверяем, что рекомендация не возвращается
        Optional<RecommendationDTO> recommendation = topSavingRuleSet.getRecommendation(USER_ID, jdbcTemplate);
        assertTrue(recommendation.isEmpty());
    }

    @Test
    public void testGetRecommendationWithInsufficientSavings() {
        // Имитация наличия дебетовых продуктов
        when(jdbcTemplate.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(Collections.singletonList(Collections.singletonMap("1", 1)));

        // Имитация отсутствия инвестиционных продуктов
        when(jdbcTemplate.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(Collections.emptyList());

        // Имитация суммы пополнений по продуктам типа SAVING
        when(jdbcTemplate.queryForObject(anyString(), eq(Double.class), eq(USER_ID)))
                .thenReturn(30_000.0);

        // Проверяем, что рекомендация не возвращается
        Optional<RecommendationDTO> recommendation = topSavingRuleSet.getRecommendation(USER_ID, jdbcTemplate);
        assertTrue(recommendation.isEmpty());
    }

    @Test
    public void testGetRecommendationWithSufficientSavings() {
        // Имитация наличия дебетовых продуктов
        when(jdbcTemplate.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(Collections.singletonList(Collections.singletonMap("1", 1)));

        // Имитация отсутствия инвестиционных продуктов
        when(jdbcTemplate.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(Collections.emptyList());

        // Имитация суммы пополнений по продуктам типа SAVING
        when(jdbcTemplate.queryForObject(anyString(), eq(Double.class), eq(USER_ID)))
                .thenReturn(60_000.0);

        // Проверяем, что рекомендация возвращается
        Optional<RecommendationDTO> recommendation = topSavingRuleSet.getRecommendation(USER_ID, jdbcTemplate);
        assertTrue(recommendation.isPresent());
        assertEquals("59efc529-2fff-41af-baff-90ccd7402925", recommendation.get().getProductId());
        assertEquals("Top Saving", recommendation.get().getRecommendation());
    }
}
