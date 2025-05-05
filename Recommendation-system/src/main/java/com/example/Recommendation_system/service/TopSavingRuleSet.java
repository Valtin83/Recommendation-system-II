package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.RecommendationDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class TopSavingRuleSet implements RecommendationRuleSet {

    private final JdbcTemplate jdbcTemplate;

    private static final String RECOMMENDATION_ID = "59efc529-2fff-41af-baff-90ccd7402925";

    public TopSavingRuleSet(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<RecommendationDTO> getRecommendation(String userId, JdbcTemplate jdbcTemplate) {
        // Проверка наличия продуктов с типом DEBIT
        String sqlDebitExist = "SELECT 1 FROM transactions t "
                + "JOIN products p ON t.product_id = p.id "
                + "WHERE t.user_id = ? AND p.type = 'DEBIT' LIMIT 1";

        List<Map<String, Object>> debitResults = this.jdbcTemplate.queryForList(sqlDebitExist, userId);
        if (debitResults.isEmpty()) {
            // Пользователь не использует продукты типа DEBIT — правило не работает
            return Optional.empty();
        }

        // Проверка отсутствия продукта INVEST у пользователя
        String sqlInvestExist = "SELECT 1 FROM transactions t "
                + "JOIN products p ON t.product_id = p.id "
                + "WHERE t.user_id = ? AND p.type = 'INVEST' LIMIT 1";

        List<Map<String, Object>> investResults = this.jdbcTemplate.queryForList(sqlInvestExist, userId);
        if (!investResults.isEmpty()) {
            // Есть инвест-продукты — правило не подходит
            return Optional.empty();
        }

        // Проверка суммы пополнений SAVING
        String sqlSavingsSum = "SELECT COALESCE(SUM(t.amount), 0) FROM transactions t "
                + "JOIN products p ON t.product_id = p.id "
                + "WHERE t.user_id = ? AND p.type = 'SAVING' AND t.type = 'DEPOSIT'";

        Double totalSavings = this.jdbcTemplate.queryForObject(sqlSavingsSum, Double.class, userId);

        if (totalSavings >= 50_000) {
            return Optional.of(new RecommendationDTO(
                    RECOMMENDATION_ID,
                    "Top Saving",
                    "Откройте свою собственную «Копилку» с нашим банком!"
            ));
        }

        return Optional.empty();
    }
}
