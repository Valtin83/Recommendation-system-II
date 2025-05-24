package com.example.Recommendation_system.model;


import jakarta.persistence.*;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "recommendation_rule")
public class Rules {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String ProductName;
    private UUID ProductId;
    private String ProductText;
    private String rule;

    public Rules(UUID id, String productName, UUID productId, String productText, String rule) {
        this.id = id;
        ProductName = productName;
        ProductId = productId;
        ProductText = productText;
        this.rule = rule;
    }

    public Rules(UUID productId) {

        ProductId = productId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public UUID getProductId() {
        return ProductId;
    }

    public void setProductId(UUID productId) {
        ProductId = productId;
    }

    public String getProductText() {
        return ProductText;
    }

    public void setProductText(String productText) {
        ProductText = productText;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rules rules = (Rules) o;
        return Objects.equals(id, rules.id) && Objects.equals(ProductName, rules.ProductName) && Objects.equals(ProductId, rules.ProductId) && Objects.equals(ProductText, rules.ProductText) && Objects.equals(rule, rules.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, ProductName, ProductId, ProductText, rule);
    }

    @Override
    public String toString() {
        return "Rules{" +
                "id=" + id +
                ", ProductName='" + ProductName + '\'' +
                ", ProductId=" + ProductId +
                ", ProductText='" + ProductText + '\'' +
                ", rule='" + rule + '\'' +
                '}';
    }
}
