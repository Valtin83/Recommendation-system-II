package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.Recommendation;

import java.util.List;

public interface RecommendationRuleSet {

    List<Recommendation> getRecommendations(String userId);
}