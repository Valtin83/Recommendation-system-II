package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.Rules;
import com.example.Recommendation_system.repository.RulesRepository;
import jakarta.persistence.EntityNotFoundException;
import org.hibernate.StaleObjectStateException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.web.client.ExpectedCount;

import java.util.Optional;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.ExpectedCount.times;

public class RulesServiceTest {

    @Mock
    private RulesRepository rulesRepository;

    @InjectMocks
    private RulesService rulesService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateRulesSuccessfully() {
        // Установка Mockito-ожиданий
        Rules rule = new Rules(UUID.randomUUID(), "Super Product", UUID.randomUUID(), "Это лучший продукт.", "Рекомендуется для всех.");

        // Mocking behavior of the repository
        when(rulesRepository.save(any(Rules.class))).thenReturn(rule);

        // Вызов метода
        Rules createdRule = rulesService.createRules();

        // Проверка результата
        assertNotNull(createdRule);
        assertEquals(rule.getProductName(), createdRule.getProductName());
        assertEquals(rule.getProductText(), createdRule.getProductText());


        verify(rulesRepository, times(1)).save(any(Rules.class));
    }

    @Test
    public void testCreateRules_StaleObjectStateException() {
        UUID ruleId = UUID.randomUUID();
        Rules existingRule = new Rules(ruleId, "Super Product", UUID.randomUUID(), "Это лучший продукт.", "Рекомендуется для всех.");

        // Expecting to throw StaleObjectStateException when save is called
        when(rulesRepository.save(any(Rules.class))).thenThrow(StaleObjectStateException.class);
        when(rulesRepository.findById(ruleId)).thenReturn(Optional.of(existingRule));

        // Вызов метода
        Rules createdRule = rulesService.createRules();

        // Проверка результата
        assertNotNull(createdRule);
        assertEquals(existingRule.getProductName(), createdRule.getProductName());
        verify(rulesRepository, times(2)).save(any(Rules.class)); // Verify that save was called twice
    }

    private Properties verify(RulesRepository rulesRepository, ExpectedCount times) {
        return null;
    }

    @Test
    public void testCreateRules_EntityNotFoundException() {
        // Simulating StaleObjectStateException when trying to save
        when(rulesRepository.save(any(Rules.class))).thenThrow(StaleObjectStateException.class);

        // Simulating the case where findById returns empty
        when(rulesRepository.findById(any(UUID.class))).thenReturn(Optional.empty());

        // Проверка на то, что метод вызовет исключение
        assertThrows(EntityNotFoundException.class, () -> rulesService.createRules());
    }
}