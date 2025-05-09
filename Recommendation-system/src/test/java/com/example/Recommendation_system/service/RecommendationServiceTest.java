package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.RecommendationDTO;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RecommendationServiceTest {

    @Test
    public void testGetRecommendations_ReturnsRecommendations() {
        // Мок JdbcTemplate
        JdbcTemplate jdbcMock = mock(JdbcTemplate.class);

        // Мок списка правил
        RecommendationRuleSet rule1 = mock(RecommendationRuleSet.class);
        RecommendationRuleSet rule2 = mock(RecommendationRuleSet.class);

        // Создаём список правил
        List<RecommendationRuleSet> rules = List.of(rule1, rule2);

        // Создаём сервис
        RecommendationService service = new RecommendationService(jdbcMock, rules);

        String userId = "test-user";

        // Настраиваем поведение правила 1 — возвращает рекомендацию
        RecommendationDTO rec1 = new RecommendationDTO("id1", "Title1", "Desc1");
        when(rule1.getRecommendation(userId, jdbcMock)).thenReturn(Optional.of(rec1));

        // правило 2 не возвращает рекомендацию
        when(rule2.getRecommendation(userId, jdbcMock)).thenReturn(Optional.empty());

        List<RecommendationDTO> result = service.getRecommendations(userId);

        // Проверка, что вернулся список с одним элементом
        assertEquals(1, result.size());
        assertEquals("id1", result.get(0).getProductId());
    }

    @Test
    public void testGetRecommendations_NoRecommendations() {
        JdbcTemplate jdbcMock = mock(JdbcTemplate.class);
        RecommendationRuleSet rule1 = mock(RecommendationRuleSet.class);
        List<RecommendationRuleSet> rules = List.of(rule1);
        RecommendationService service = new RecommendationService(jdbcMock, rules);
        String userId = "user";

        // Все правила возвращают пустой Optional
        when(rule1.getRecommendation(userId, jdbcMock)).thenReturn(Optional.empty());

        List<RecommendationDTO> result = service.getRecommendations(userId);

        // Должен быть пустой список
        assertTrue(result.isEmpty());
    }
}

