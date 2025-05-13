package com.example.Recommendation_system.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;


import java.util.Objects;

@Entity
@Table(name = "rule_conditions")
public class RuleCondition {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    private String query;
    private String arguments; // json строка
    private boolean negate;

    @ManyToOne
    @JoinColumn(name = "dynamic_rule_id")
    @JsonBackReference
    private DynamicRule dynamicRule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getArguments() {
        return arguments;
    }

    public void setArguments(String arguments) {
        this.arguments = arguments;
    }

    public boolean isNegate() {
        return negate;
    }

    public void setNegate(boolean negate) {
        this.negate = negate;
    }

    public DynamicRule getDynamicRule() {
        return dynamicRule;
    }

    public void setDynamicRule(DynamicRule dynamicRule) {
        this.dynamicRule = dynamicRule;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RuleCondition that = (RuleCondition) o;
        return negate == that.negate && Objects.equals(id, that.id) && Objects.equals(query, that.query) && Objects.equals(arguments, that.arguments) && Objects.equals(dynamicRule, that.dynamicRule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, query, arguments, negate, dynamicRule);
    }


}
