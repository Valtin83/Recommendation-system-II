package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.Recommendation;
import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class Invest500RuleSet implements RecommendationRuleSet {

    private final JdbcTemplate jdbcTemplate;
    final String DESCRIPTION = "Откройте свой путь к успеху с индивидуальным инвестиционным счетом (ИИС) от нашего банка! Воспользуйтесь налоговыми льготами и начните инвестировать с умом";
    private final Cache<String, Boolean> cache;
    final String NO_RECOMMENDATION = "No recommendation";

    public Invest500RuleSet(JdbcTemplate jdbcTemplate, Cache<String, Boolean> cache) {
        this.jdbcTemplate = jdbcTemplate;
        this.cache = cache;
    }

    @Override
    public Optional<Object> getRecommendation(UUID userId) {
        // Генерируем ключи для кеша
        String cacheKeyDebit = userId + "_INVEST_DEBIT_COUNT";
        String cacheKeyInvest = userId + "_INVEST_PRODUCT_COUNT";
        String cacheKeySaving = userId + "_SAVING_SUM_OVER_1000";

        // Проверяем кеш или выполняем запросы
        Boolean usesDebit = cache.getIfPresent(cacheKeyDebit);
        if (usesDebit == null) {
            usesDebit = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) > 0 FROM TRANSACTIONS t " +
                            "JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                            "WHERE t.USER_ID = ? AND p.TYPE = 'DEBIT'",
                    Boolean.class,
                    userId
            );
            assert usesDebit != null;
            cache.put(cacheKeyDebit, usesDebit);
        }

        Boolean noInvestProducts = cache.getIfPresent(cacheKeyInvest);
        if (noInvestProducts == null) {
            noInvestProducts = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) = 0 FROM TRANSACTIONS t " +
                            "JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                            "WHERE t.USER_ID = ? AND p.TYPE = 'INVEST'",
                    Boolean.class,
                    userId
            );
            assert noInvestProducts != null;
            cache.put(cacheKeyInvest, noInvestProducts);
        }

        Boolean savingOver1000 = cache.getIfPresent(cacheKeySaving);
        if (savingOver1000 == null) {
            savingOver1000 = jdbcTemplate.queryForObject(
                    "SELECT COALESCE(SUM(t.AMOUNT), 0) > 1000 " +
                            "FROM TRANSACTIONS t " +
                            "JOIN PRODUCTS p ON t.PRODUCT_ID = p.ID " +
                            "WHERE t.USER_ID = ? AND p.TYPE = 'SAVING' AND t.TYPE = 'DEPOSIT'",
                    Boolean.class,
                    userId
            );
            assert savingOver1000 != null;
            cache.put(cacheKeySaving, savingOver1000);
        }

        // Формируем рекомендацию на основе кешированных данных
        if (usesDebit || noInvestProducts || savingOver1000) {
            return Optional.of(List.of(
                    new Recommendation(userId, "Invest 500", DESCRIPTION)
            ));
        }
        return Optional.of(List.of(
                new Recommendation(userId, "Invest 500", NO_RECOMMENDATION)
        ));
    }
}