package com.example.Recommendation_system.controller;

import com.example.Recommendation_system.model.DynamicRule;
import com.example.Recommendation_system.service.DynamicRuleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/rule")
public class DynamicRuleController {
    private final DynamicRuleService service;

    public DynamicRuleController(DynamicRuleService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<DynamicRule> addRule(@RequestBody DynamicRule rule) {
        DynamicRule savedRule = service.createRule(rule);
        return ResponseEntity.ok(savedRule);
    }

    @GetMapping("/getRules")
    public List<DynamicRule> getRules() {
        List<DynamicRule> getRules = service.getAllRules();
        return getRules;
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRule(@PathVariable UUID id) {
        boolean isDeleted = service.deleteRule(id);
        if (isDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
