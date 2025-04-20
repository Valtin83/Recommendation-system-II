package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.Recommendation;

import java.util.List;

public class SimpleCreditRuleSet implements RecommendationRuleSet {
    @Override
    public List<Recommendation> getRecommendations(String userId) {
        return List.of(new Recommendation("ab138afb-f3ba-4a93-b74f-0fcee86d447f",
                "Простой кредит",
                "Откройте мир выгодных кредитов с нами! Ищете способ быстро и без лишних хлопот получить нужную сумму?",
                List.of("Пользователь не использует продукты с типом CREDIT.")));
    }
}
