package com.example.Recommendation_system.controller;

import com.github.benmanes.caffeine.cache.Cache;
import org.springframework.boot.info.BuildProperties;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/management")
public class ManagementController {    //сброс кеша
    private final Cache<String, Boolean> queryResultCache;
    private final BuildProperties buildProperties;

    public ManagementController(Cache<String, Boolean> queryResultCache, BuildProperties buildProperties) {
        this.queryResultCache = queryResultCache;
        this.buildProperties = buildProperties;
    }

    @PostMapping("/clear-caches")
    public ResponseEntity<?> clearCaches() {
        queryResultCache.invalidateAll();
        return ResponseEntity.ok().body(Map.of("status", "Cache cleared"));
    }

    @GetMapping("/info")
    public ResponseEntity<?> getServiceInfo() {
        return ResponseEntity.ok().body(Map.of(
                "name", buildProperties.getName(),
                "version", buildProperties.getVersion()
        ));
    }
}
