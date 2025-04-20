package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.Recommendation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopSavingRuleSet implements RecommendationRuleSet {

    @Override
    public List<Recommendation> getRecommendations(String userId) {
        return List.of(new Recommendation("59efc529-2fff-41af-baff-90ccd7402925",
                "Top Saving",
                "Откройте свою собственную «Копилку» с нашим банком! «Копилка» — это уникальный банковский инструмент, который поможет вам легко и удобно накапливать деньги на важные цели.",
                List.of("Пользователь использует как минимум один продукт с типом DEBIT.")));
    }
}
