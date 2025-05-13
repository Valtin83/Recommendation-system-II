package com.example.Recommendation_system.controller;

import com.example.Recommendation_system.model.RuleStatistic;
import com.example.Recommendation_system.repository.RuleStatisticRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/rule")
public class RuleStatisticController {                                          //контроллер для статистики
    private final RuleStatisticRepository statisticRepository;

    public RuleStatisticController(RuleStatisticRepository statisticRepository) {
        this.statisticRepository = statisticRepository;
    }

    @GetMapping("/stats")
    public ResponseEntity<?> getStatistics() {
        List<RuleStatistic> stats = statisticRepository.findAll();

        return ResponseEntity.ok().body(
                Map.of("stats", stats.stream()
                        .map(stat -> Map.of(
                                "rule_id", stat.getRuleId(),
                                "count", stat.getCount()
                        ))
                        .collect(Collectors.toList())
                )
        );
    }
}