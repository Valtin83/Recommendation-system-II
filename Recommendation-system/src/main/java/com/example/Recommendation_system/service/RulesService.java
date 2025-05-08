package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.Rules;
import com.example.Recommendation_system.repository.RulesRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.hibernate.StaleObjectStateException;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class RulesService {
    private RulesRepository rulesRepository;

    public RulesService(RulesRepository rulesRepository) {
        this.rulesRepository = rulesRepository;
    }

    @Transactional
    public Rules createRules() {
        UUID id = UUID.randomUUID();
        String productName = "Super Product";
        UUID productId = UUID.randomUUID();
        String productText = "Это лучший продукт.";
        String rules = "Рекомендуется для всех.";

        // Создание нового объекта Rules
        Rules rule = new Rules(id, productName, productId, productText, rules);

        System.out.println(rule);

        // Сохранение объекта в репозитории
        try {
            rulesRepository.save(rule);  // Исправлено на использование объекта rule
        } catch (StaleObjectStateException e) {
            // Логирование и перезагрузка данных перед сохранением
            AtomicReference<Rules> refreshedRules = new AtomicReference<>(rulesRepository.findById(rule.getId())  // Используем rule.getId()
                    .orElseThrow(() -> new EntityNotFoundException("Правила не найдены")));
            // Обновить данные и сохранить снова, если необходимо
            // Например, можно скопировать данные из refreshedRules в rule и повторить сохранение
            rule = refreshedRules.get();
            rulesRepository.save(rule);
        }
        return rule;
    }
}
