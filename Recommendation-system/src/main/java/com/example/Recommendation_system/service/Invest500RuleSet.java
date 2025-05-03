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
        // Проверяем наличие транзакций DEBIT
        boolean hasDebitTransactions = hasTransactionsOfType(userId);
        if (!hasDebitTransactions) {
            return Optional.empty(); // Нет транзакций DEBIT
        }

        // Проверяем наличие продуктов типа INVEST
        boolean hasInvestProducts = hasProductsOfType(userId, "INVEST");
        if (hasInvestProducts) {
            return Optional.empty(); // Подписан на продукты INVEST
        }

        // Рассчитываем суммарное пополнение по продуктам типа SAVING
        double totalSavings = getTotalSavings(userId);
        if (totalSavings > 1000) {
            return Optional.of(new RecommendationDTO(
                    RECOMMENDATION_ID,
                    "Invest 500",
                    "Откройте путь к успеху с ИИС и налоговыми льготами!"
            ));
        }

        return Optional.empty(); // Условия не выполнены
    }

    // Метод для проверки наличия транзакций определенного типа
    private boolean hasTransactionsOfType(String userId) {
        String sql = "SELECT 1 FROM transactions t "
                + "JOIN products p ON t.product_id = p.id "
                + "WHERE t.user_id = ? AND p.type = ? LIMIT 1";
        List<Map<String, Object>> results = this.jdbcTemplate.queryForList(sql, userId, "DEBIT");
        return !results.isEmpty();
    }

    // Метод для проверки наличия продуктов определенного типа
    private boolean hasProductsOfType(String userId, String type) {
        String sql = "SELECT 1 FROM products p "
                + "JOIN transactions t ON t.product_id = p.id "
                + "WHERE t.user_id = ? AND p.type = ? LIMIT 1";
        List<Map<String, Object>> results = this.jdbcTemplate.queryForList(sql, userId, type);
        return !results.isEmpty();
    }

    // Метод для получения суммы пополнений по продуктам типа SAVING
    private double getTotalSavings(String userId) {
        String sql = "SELECT COALESCE(SUM(t.amount), 0) FROM transactions t "
                + "JOIN products p ON t.product_id = p.id "
                + "WHERE t.user_id = ? AND p.type = 'SAVING' AND t.type = 'DEPOSIT'";
        return this.jdbcTemplate.queryForObject(sql, Double.class, userId);
    }
}