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
    private String product_name;
    private UUID product_id;
    private String product_text;
    private String rule;

    @Version
    private int version;

    public Rules(UUID id, String product_name, UUID product_id, String product_text, String rule) {
        this.id = id;
        this.product_name = product_name;
        this.product_id = product_id;
        this.product_text = product_text;
        this.rule = rule;
    }

    public Rules() {

    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public UUID getProduct_id() {
        return product_id;
    }

    public void setProduct_id(UUID product_id) {
        this.product_id = product_id;
    }

    public String getProduct_text() {
        return product_text;
    }

    public void setProduct_text(String product_text) {
        this.product_text = product_text;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Rules rules = (Rules) o;
        return version == rules.version && Objects.equals(id, rules.id) && Objects.equals(product_name, rules.product_name) && Objects.equals(product_id, rules.product_id) && Objects.equals(product_text, rules.product_text) && Objects.equals(rule, rules.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product_name, product_id, product_text, rule, version);
    }

    @Override
    public String toString() {
        return "Rules{" +
                "id=" + id +
                ", product_name='" + product_name + '\'' +
                ", product_id=" + product_id +
                ", product_text='" + product_text + '\'' +
                ", rule='" + rule + '\'' +
                ", version=" + version +
                '}';
    }
}
