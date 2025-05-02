package com.example.Recommendation_system.model;

import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;


public class Recommendation  {

    @Id
    private String userId;
    private List<RecommendationDTO> recommendations;

    public Recommendation(String userId, List<RecommendationDTO> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<RecommendationDTO> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<RecommendationDTO> recommendations) {
        this.recommendations = recommendations;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return Objects.equals(userId, that.userId) && Objects.equals(recommendations, that.recommendations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, recommendations);
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "userId='" + userId + '\'' +
                ", recommendations=" + recommendations +
                '}';
    }
}