package com.example.Recommendation_system.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.hasSize;

import com.example.Recommendation_system.model.RuleStatistic;
import com.example.Recommendation_system.repository.RuleStatisticRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

@WebMvcTest(RuleStatisticController.class)
public class RuleStatisticControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private RuleStatisticRepository statisticRepository;

    @InjectMocks
    private RuleStatisticController statisticController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetStatistics() throws Exception {

        RuleStatistic stat1 = new RuleStatistic();
        RuleStatistic stat2 = new RuleStatistic();
        List<RuleStatistic> stats = Arrays.asList(stat1, stat2);

        when(statisticRepository.findAll()).thenReturn(stats);

        // When: making a GET request to /rule/stats
        mockMvc.perform(get("/rule/stats")
                        .contentType(MediaType.APPLICATION_JSON))
                // Then: expect a 200 OK status and the correct JSON response
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stats", hasSize(2)))
                .andExpect(jsonPath("$.stats[0].rule_id").value(1))
                .andExpect(jsonPath("$.stats[0].count").value(10))
                .andExpect(jsonPath("$.stats[1].rule_id").value(2))
                .andExpect(jsonPath("$.stats[1].count").value(20));
    }
}
