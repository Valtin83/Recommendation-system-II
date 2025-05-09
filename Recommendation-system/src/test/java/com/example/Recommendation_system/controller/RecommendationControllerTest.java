package com.example.Recommendation_system.controller;

import com.example.Recommendation_system.model.RecommendationDTO;
import com.example.Recommendation_system.service.RecommendationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RecommendationController.class)
public class RecommendationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    public RecommendationService recommendationService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetRecommendations_ReturnsRecommendations() throws Exception {
        String userId = "test-user";

        // Создаем мок-данные
        List<RecommendationDTO> mockRecommendations = List.of(
                new RecommendationDTO("id1", "Title1", "Description1"),
                new RecommendationDTO("id2", "Title2", "Description2")
        );

        // Настраиваем поведение mocks
        when(recommendationService.getRecommendations(userId))
                .thenReturn(mockRecommendations);

        // Выполняем GET-запрос
        mockMvc.perform(get("/recommendations/{userId}", userId))
                .andExpect(status().isOk())
                // Проверяем содержимое JSON
                .andExpect(jsonPath("$.userId").value(userId))
                .andExpect(jsonPath("$.recommendations").isArray())
                .andExpect(jsonPath("$.recommendations.length()").value(2));
    }

    @Test
    public void testGetRecommendations_EmptyList() throws Exception {
        String userId = "test-user";

        when(recommendationService.getRecommendations(userId))
                .thenReturn(List.of()); // пустой список рекомендаций

        mockMvc.perform(get("/recommendations/{userId}", userId))
                .andExpect(status().isOk())
                // Проверка, что recommendations пустой
                .andExpect(jsonPath("$.recommendations").isEmpty());
    }
}

