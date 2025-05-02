package com.example.Recommendation_system.model;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public class RecommendationDTO {

    @Id
    private String userId;
    private String recommendation;
    private String description;

    public RecommendationDTO(String userId, String recommendation, String description) {
        this.userId = userId;
        this.recommendation = recommendation;
        this.description = description;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRecommendationText() {
        return recommendation;
    }

    public void setRecommendationText(String recommendationText) {
        this.recommendation = recommendation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RecommendationDTO that = (RecommendationDTO) o;
        return Objects.equals(userId, that.userId) && Objects.equals(recommendation, that.recommendation) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, recommendation, description);
    }

    @Override
    public String toString() {
        return "RecommendationDTO{" +
                "userId='" + userId + '\'' +
                ", recommendation='" + recommendation + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
