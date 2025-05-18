package com.example.Recommendation_system.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class RecommendationService {

    private final List<RecommendationRuleSet> ruleSets;

    public RecommendationService(List<RecommendationRuleSet> ruleSets) {
        this.ruleSets = ruleSets;
    }

    public List<Object> getListRecommendation(String userId) {
        return ruleSets.stream()
                .flatMap(ruleSet -> ruleSet.getRecommendation(UUID.fromString(userId)).stream())
                .collect(Collectors.toList());
    }


}