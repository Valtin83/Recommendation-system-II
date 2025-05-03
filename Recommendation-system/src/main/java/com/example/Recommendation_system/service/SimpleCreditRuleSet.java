package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.RecommendationDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class SimpleCreditRuleSet implements RecommendationRuleSet {

    private static final String RECOMMENDATION_ID = "ab138afb-f3ba-4a93-b74f-0fcee86d447f";

    private final JdbcTemplate jdbcTemplate;

    public SimpleCreditRuleSet(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<RecommendationDTO> getRecommendation(String userId, JdbcTemplate jdbcTemplate) {
        // Проверка отсутствия продуктов с типом CREDIT
        String sqlCheckCredit = "SELECT 1 FROM transactions t "
                + "JOIN products p ON t.product_id = p.id "
                + "WHERE t.user_id = ? AND p.type = 'CREDIT' LIMIT 1";

        List<Map<String, Object>> creditResults = this.jdbcTemplate.queryForList(sqlCheckCredit, userId);
        if (!creditResults.isEmpty()) {
            // Есть продукты CREDIT — рекомендация не подходит
            return Optional.empty(); // Нет необходимости в кредитах для пользователя
        }

        // Сумма депозита
        Double sumDeposit = this.jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(t.amount), 0) FROM transactions t "
                        + "JOIN products p ON t.product_id = p.id "
                        + "WHERE t.user_id = ? AND p.type = 'DEBIT' AND t.type = 'DEPOSIT'",
                Double.class, userId);

        // Сумма трат
        Double sumSpend = this.jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(t.amount), 0) FROM transactions t "
                        + "JOIN products p ON t.product_id = p.id "
                        + "WHERE t.user_id = ? AND p.type = 'DEBIT' AND t.type = 'SPEND'",
                Double.class, userId);

        // Сумма расходов
        Double totalExpenses = this.jdbcTemplate.queryForObject(
                "SELECT COALESCE(SUM(t.amount), 0) FROM transactions t "
                        + "JOIN products p ON t.product_id = p.id "
                        + "WHERE t.user_id = ? AND p.type = 'DEBIT' AND t.type = 'SPEND'",
                Double.class, userId);

        // Проверка условий
        if (sumDeposit > sumSpend && totalExpenses > 100_000) {
            return Optional.of(new RecommendationDTO(
                    RECOMMENDATION_ID,
                    "Simple Credit",
                    "Откройте мир выгодных кредитов с нами!"
            ));
        }

        return Optional.empty();
    }
}