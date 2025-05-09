package com.example.Recommendation_system.model;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "dynamic_rules")
public class DynamicRule {
    @Id
    @GeneratedValue(generator = "UUID")
    @org.hibernate.annotations.GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
     private UUID id;

    private String productName;
    private UUID productId;
    private String productText;

    @OneToMany(mappedBy = "dynamicRule", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RuleCondition> conditions;

    public DynamicRule() {}

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public UUID getProductId() {
        return productId;
    }

    public void setProductId(UUID productId) {
        this.productId = productId;
    }

    public String getProductText() {
        return productText;
    }

    public void setProductText(String productText) {
        this.productText = productText;
    }

    public List<RuleCondition> getConditions() {
        return conditions;
    }

    public void setConditions(List<RuleCondition> conditions) {
        this.conditions = conditions;
    }
}