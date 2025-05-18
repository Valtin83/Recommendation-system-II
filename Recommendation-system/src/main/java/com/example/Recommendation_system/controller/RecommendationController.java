package com.example.Recommendation_system.controller;

import com.example.Recommendation_system.service.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/recommendations")
public class RecommendationController {

    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Object>> getRecommendations(@PathVariable UUID userId) {
        List<Object> recommendations = Collections.singletonList(recommendationService.getListRecommendation(String.valueOf(userId)));
        return ResponseEntity.ok(recommendations);
    }

}

