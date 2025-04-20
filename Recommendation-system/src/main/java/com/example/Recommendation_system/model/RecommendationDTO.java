package com.example.Recommendation_system.model;

import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;

public class RecommendationDTO {

    @Id
    private String userId;
    private List<Recommendation> recommendations;

    public RecommendationDTO(String userId, List<Recommendation> recommendations) {
        this.userId = userId;
        this.recommendations = recommendations;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Recommendation> getRecommendations() {
        return recommendations;
    }

    public void setRecommendations(List<Recommendation> recommendations) {
        this.recommendations = recommendations;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecommendationDTO that = (RecommendationDTO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(recommendations, that.recommendations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, recommendations);
    }

    @Override
    public String toString() {
        return "RecommendationDTO{" +
                "userId=" + userId +
                ", recommendations=" + recommendations +
                '}';
    }
}
