package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.RecommendationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class Invest500RuleSetTest {

    private static final String USER_ID = "user123";

    @Test
    public void testGetRecommendation_WhenAllConditionsMet_ReturnsRecommendation() {
        // Мокаем JdbcTemplate
        JdbcTemplate jdbcMock = mock(JdbcTemplate.class);
        Invest500RuleSet rule = new Invest500RuleSet(jdbcMock);

        // Мокаем результат для наличия транзакций DEBIT
        when(jdbcMock.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(List.of(Map.of("dummy", "value"))); // хоть что-то для дебит

        // Мокаем наличие INVEST-продуктов - возвращаем результат, чтобы условие не возвращало пусто
        when(jdbcMock.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(List.of()); // для DEBIT
        doReturn(List.of()).when(jdbcMock).queryForList(anyString(), eq(USER_ID)); // для INVEST

        // Мокаем сумму SAVING deposits
        when(jdbcMock.queryForObject(anyString(), eq(Double.class), eq(USER_ID)))
                .thenReturn(1500.0);

        // Выполняем
        Optional<RecommendationDTO> recOpt = rule.getRecommendation(USER_ID, jdbcMock);

        assertEquals(true, recOpt.isPresent());
        assertEquals("Invest 500", recOpt.get().getRecommendation());
    }

    @Test
    public void testGetRecommendation_WhenNoDebitTransactions_ReturnsEmpty() {
        JdbcTemplate jdbcMock = mock(JdbcTemplate.class);
        Invest500RuleSet rule = new Invest500RuleSet(jdbcMock);

        when(jdbcMock.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(List.of()); // Нет транзакций DEBIT

        Optional<RecommendationDTO> recOpt = rule.getRecommendation(USER_ID, jdbcMock);
        assertTrue(recOpt.isEmpty());
    }

    @Test
    public void testGetRecommendation_WhenInvestExists_ReturnsEmpty() {
        JdbcTemplate jdbcMock = mock(JdbcTemplate.class);
        Invest500RuleSet rule = new Invest500RuleSet(jdbcMock);

        // Для DEBIT - есть
        when(jdbcMock.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(List.of(Map.of("dummy", "value")));
        // Для INVEST - есть
        when(jdbcMock.queryForList(contains("INVEST"), eq(USER_ID)))
                .thenReturn(List.of(Map.of("dummy", "value")));

        Optional<RecommendationDTO> recOpt = rule.getRecommendation(USER_ID, jdbcMock);
        assertTrue(recOpt.isEmpty());
    }

    @Test
    public void testGetRecommendation_WhenSavingsBelowThreshold_ReturnsEmpty() {
        JdbcTemplate jdbcMock = mock(JdbcTemplate.class);
        Invest500RuleSet rule = new Invest500RuleSet(jdbcMock);

        // Для DEBIT - есть транзакции
        when(jdbcMock.queryForList(anyString(), eq(USER_ID)))
                .thenReturn(List.of(Map.of("dummy", "value")));
        // Для INVEST - нет
        when(jdbcMock.queryForList(contains("INVEST"), eq(USER_ID)))
                .thenReturn(List.of());
        // Для суммы savings - ниже 1000
        when(jdbcMock.queryForObject(anyString(), eq(Double.class), eq(USER_ID)))
                .thenReturn(900.0);

        Optional<RecommendationDTO> recOpt = rule.getRecommendation(USER_ID, jdbcMock);
        assertTrue(recOpt.isEmpty());
    }
}
