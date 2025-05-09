package com.example.Recommendation_system.service;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final JdbcTemplate jdbcTemplate;

    public UserService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public UUID findUserIdByUsername(String username) {
        String sql = "SELECT id FROM users WHERE username = ?";
        try {
            return jdbcTemplate.queryForObject(sql, UUID.class, username);
        } catch (Exception e) {
            return null;
        }
    }

    public String getUserFullName(UUID userId) {
        String sql = "SELECT first_name || ' ' || last_name FROM users WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, String.class, userId);
        } catch (Exception e) {
            return "Уважаемый клиент";
        }
    }
}
