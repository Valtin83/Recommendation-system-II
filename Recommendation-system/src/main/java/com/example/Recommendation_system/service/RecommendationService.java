package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.RecommendationDTO;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {

    private final JdbcTemplate jdbcTemplate;
    private final List<RecommendationRuleSet> rules;

    public RecommendationService(JdbcTemplate jdbcTemplate, List<RecommendationRuleSet> rules) {
        this.jdbcTemplate = jdbcTemplate;
        this.rules = rules;
    }

    public List<RecommendationDTO> getRecommendations(String userId) {
        List<RecommendationDTO> result = new ArrayList<>();
        for (RecommendationRuleSet rule : rules) {
            Optional<RecommendationDTO> recommendation = rule.getRecommendation(userId, jdbcTemplate);
            recommendation.ifPresent(result::add);
        }
        return result;
    }
}