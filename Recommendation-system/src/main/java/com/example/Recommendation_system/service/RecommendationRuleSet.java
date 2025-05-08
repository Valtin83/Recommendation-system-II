package com.example.Recommendation_system.service;

import java.util.Optional;
import java.util.UUID;

public interface RecommendationRuleSet {

    Optional<Object> getRecommendation(UUID userId);
}
