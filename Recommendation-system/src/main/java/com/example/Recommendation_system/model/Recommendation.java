package com.example.Recommendation_system.model;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.annotation.Id;

import java.util.List;
import java.util.Objects;

@EntityScan
public class Recommendation {

    @Id
    private String id;
    private String title;
    private String description;
    private List<String> ruleSets;

    public Recommendation(String id, String title, String description, List<String> ruleSets) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.ruleSets = ruleSets;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getRuleSets() {
        return ruleSets;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Recommendation that = (Recommendation) o;
        return Objects.equals(id, that.id) && Objects.equals(title, that.title) && Objects.equals(description, that.description) && Objects.equals(ruleSets, that.ruleSets);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, ruleSets);
    }

    @Override
    public String toString() {
        return "Recommendation{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", ruleSets=" + ruleSets +
                '}';
    }
}