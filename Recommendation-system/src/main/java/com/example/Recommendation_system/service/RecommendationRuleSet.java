package com.example.Recommendation_system.service;

import com.example.Recommendation_system.model.RecommendationDTO;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;

public interface RecommendationRuleSet {

    Optional<RecommendationDTO> getRecommendation(String userId, JdbcTemplate jdbcTemplate);

}
