package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.RecommendationDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class Invest500RuleSet implements RecommendationRuleSet {

    private final JdbcTemplate jdbcTemplate;

    private static final String RECOMMENDATION_ID = "147f6a0f-3b91-413b-ab99-87f081d60d5a";

    public Invest500RuleSet(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Optional<RecommendationDTO> getRecommendation(String userId, JdbcTemplate jdbcTemplate) {
        // Проверка, есть ли у пользователя транзакции с продуктом типа DEBIT
        String sqlDebit = "SELECT 1 FROM transactions t "
                + "JOIN products p ON t.product_id = p.id "
                + "WHERE t.user_id = ? AND p.type = 'DEBIT' LIMIT 1";

        List<Map<String, Object>> debitResults = this.jdbcTemplate.queryForList(sqlDebit, userId);
        if (debitResults.isEmpty()) {
            // Нет транзакций с DEBIT, правило не выполняется
            return Optional.empty();
        }

        // Проверка, есть ли у пользователя продукты типа INVEST (если есть, правило не выполняется)
        String sqlInvest = "SELECT 1 FROM transactions t "
                + "JOIN products p ON t.product_id = p.id "
                + "WHERE t.user_id = ? AND p.type = 'INVEST' LIMIT 1";

        List<Map<String, Object>> investResults = this.jdbcTemplate.queryForList(sqlInvest, userId);
        if (!investResults.isEmpty()) {
            // Есть продукты INVEST, правило не выполняется
            return Optional.empty();
        }

        // Сумма пополнений по продуктам типа SAVING
        String sqlSavings = "SELECT SUM(t.amount) FROM transactions t "
                + "JOIN products p ON t.product_id = p.id "
                + "WHERE t.user_id = ? AND p.type = 'SAVING' AND t.type = 'DEPOSIT'";

        Double totalSavings = this.jdbcTemplate.queryForObject(sqlSavings, Double.class, userId);
        totalSavings = (totalSavings != null) ? totalSavings : 0.0;

        if (totalSavings > 1000) {
            return Optional.of(new RecommendationDTO(
                    RECOMMENDATION_ID,
                    "Invest 500",
                    "Откройте путь к успеху с ИИС и налоговыми льготами!"
            ));
        }

        return Optional.empty();
    }

}
