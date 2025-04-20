package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.Recommendation;
import com.example.Recommendation_system.model.RecommendationDTO;
import com.example.Recommendation_system.repository.RecommendationsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {

    private final RecommendationsRepository recommendationRepository;
    private final List<RecommendationRuleSet> rules;

    @Autowired
    public RecommendationService(RecommendationsRepository recommendationRepository,
                                 List<RecommendationRuleSet> rules) {
        this.recommendationRepository = recommendationRepository;
        this.rules = rules;
    }

    public RecommendationDTO getRecommendations(String userId) {
        List<Recommendation> recommendations = new ArrayList<>();
        for (RecommendationRuleSet rule : rules) {
            List<Recommendation> ruleRecommendations = rule.getRecommendations(userId);
            if (ruleRecommendations != null) {
                recommendations.addAll(ruleRecommendations);
            }
        }
        return new RecommendationDTO(userId, recommendations);
    }
}