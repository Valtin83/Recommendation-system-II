package com.example.Recommendation_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.example.recommendation_system.model")
public class RecommendationSystemIiApplication {

	public static void main(String[] args) {
		SpringApplication.run(RecommendationSystemIiApplication.class, args);
	}

}
