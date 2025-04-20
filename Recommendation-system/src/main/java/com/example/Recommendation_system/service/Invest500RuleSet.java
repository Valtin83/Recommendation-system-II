package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.Recommendation;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Invest500RuleSet implements RecommendationRuleSet {

    @Override
    public List<Recommendation> getRecommendations(String userId) {
        // Логика проверки правил и создание списков рекомендаций
        // В данном случае возвращается примерный список
        return List.of(new Recommendation("147f6a0f-3b91-413b-ab99-87f081d60d5a",
                "Invest 500",
                "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом. Пополните счет до конца года и получите выгоду в виде вычета на взнос в следующем налоговом периоде.",
                List.of("Пользователь использует как минимум один продукт с типом DEBIT.")));
    }

}

