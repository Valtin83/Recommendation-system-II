package com.example.Recommendation_system.model;

import org.springframework.data.annotation.Id;

import java.util.Objects;

public class RecommendationDTO {

    @Id
    private String productId;
    private String recommendation;
    private String description;

    public RecommendationDTO(String userId, String recommendation, String description) {
        this.productId = userId;
        this.recommendation = recommendation;
        this.description = description;
    }

    public String getProductId() {
        return productId;
    }

    public void setUserId(String userId) {
        this.productId = userId;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendationText) {
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
        return Objects.equals(productId, that.productId) && Objects.equals(recommendation, that.recommendation) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, recommendation, description);
    }

    @Override
    public String toString() {
        return "RecommendationDTO{" +
                "productId='" + productId + '\'' +
                ", recommendation='" + recommendation + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
