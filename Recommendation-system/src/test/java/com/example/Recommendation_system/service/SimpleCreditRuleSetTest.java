package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.RecommendationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class SimpleCreditRuleSetTest {

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private SimpleCreditRuleSet simpleCreditRuleSet;

    private static final String USER_ID = "test-user";

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetRecommendationWithCreditProduct() {
        // Настройка мока, чтобы имелся хотя бы один кредитный продукт
        when(jdbcTemplate.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(Collections.singletonList(Collections.singletonMap("1", 1)));

        // Проверяем, что рекомендация отсутствует
        Optional<RecommendationDTO> recommendation = simpleCreditRuleSet.getRecommendation(USER_ID, jdbcTemplate);
        assertTrue(recommendation.isEmpty());
    }

    @Test
    public void testGetRecommendationWithoutCreditProduct() {
        // Настройка мока, чтобы не было кредитных продуктов
        when(jdbcTemplate.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(Collections.emptyList());

        // Настройка тестовых данных для депозитов и трат
        when(jdbcTemplate.queryForObject(anyString(), eq(Double.class), eq(USER_ID)))
                .thenReturn(150000.0) // сумма депозитов
                .thenReturn(40000.0) // сумма трат
                .thenReturn(120000.0); // сумма расходов

        // Проверяем наличие рекомендации
        Optional<RecommendationDTO> recommendation = simpleCreditRuleSet.getRecommendation(USER_ID, jdbcTemplate);
        assertTrue(recommendation.isPresent());
        assertEquals("ab138afb-f3ba-4a93-b74f-0fcee86d447f", recommendation.get().getProductId());
        assertEquals("Simple Credit", recommendation.get().getRecommendation());
        assertEquals("Откройте мир выгодных кредитов с нами!", recommendation.get().getDescription());
    }

    @Test
    public void testGetRecommendationWithoutCreditProductInsufficientExpenses() {
        // Настройка мока, чтобы не было кредитных продуктов
        when(jdbcTemplate.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(Collections.emptyList());

        // Настройка тестовых данных для депозитов и трат
        when(jdbcTemplate.queryForObject(anyString(), eq(Double.class), eq(USER_ID)))
                .thenReturn(150000.0) // сумма депозитов
                .thenReturn(40000.0) // сумма трат
                .thenReturn(90000.0); // сумма расходов

        // Проверяем отсутствие рекомендации
        Optional<RecommendationDTO> recommendation = simpleCreditRuleSet.getRecommendation(USER_ID, jdbcTemplate);
        assertTrue(recommendation.isEmpty());
    }

    @Test
    public void testGetRecommendationWithoutCreditProductTooLowSavings() {
        // Настройка мока, чтобы не было кредитных продуктов
        when(jdbcTemplate.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(Collections.emptyList());

        // Настройка тестовых данных для депозитов и трат
        when(jdbcTemplate.queryForObject(anyString(), eq(Double.class), eq(USER_ID)))
                .thenReturn(50000.0) // сумма депозитов
                .thenReturn(50000.0) // сумма трат
                .thenReturn(200000.0); // сумма расходов

        // Проверяем отсутствие рекомендации
        Optional<RecommendationDTO> recommendation = simpleCreditRuleSet.getRecommendation(USER_ID, jdbcTemplate);
        assertTrue(recommendation.isEmpty());
    }
}