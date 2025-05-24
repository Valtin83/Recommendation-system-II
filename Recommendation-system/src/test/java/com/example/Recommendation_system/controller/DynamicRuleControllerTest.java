package com.example.Recommendation_system.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.example.Recommendation_system.model.DynamicRule;
import com.example.Recommendation_system.service.DynamicRuleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class DynamicRuleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private DynamicRuleService dynamicRuleService;

    @InjectMocks
    private DynamicRuleController dynamicRuleController;

    private DynamicRule testRule;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(dynamicRuleController).build();

        testRule = new DynamicRule();  // Create a test dynamic rule object, assuming suitable constructor/setters
        testRule.setId(UUID.randomUUID());
        // Set other fields as necessary
    }

    @Test
    public void addRule_ShouldReturnSavedRule() throws Exception {
        when(dynamicRuleService.createRule(any(DynamicRule.class))).thenReturn(testRule);

        mockMvc.perform(post("/rule")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"" + testRule.getId() + "\"}") // Update the JSON as per your rule fields
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testRule.getId().toString())); // Check if the correct ID is returned
    }

    @Test
    public void getRules_ShouldReturnListOfRules() throws Exception {
        List<DynamicRule> rulesList = new ArrayList<>();
        rulesList.add(testRule);

        when(dynamicRuleService.getAllRules()).thenReturn(rulesList);

        mockMvc.perform(get("/rule/getRules")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(testRule.getId().toString())); // Validate the response contains the expected rule
    }

    @Test
    public void deleteRule_ShouldReturnNoContent_WhenDeleted() throws Exception {
        UUID ruleId = UUID.randomUUID();
        when(dynamicRuleService.deleteRule(ruleId)).thenReturn(true);

        mockMvc.perform(delete("/rule/{id}", ruleId))
                .andExpect(status().isNoContent()); // Check that the response indicates no content
    }

    @Test
    public void deleteRule_ShouldReturnNotFound_WhenRuleDoesNotExist() throws Exception {
        UUID ruleId = UUID.randomUUID();
        when(dynamicRuleService.deleteRule(ruleId)).thenReturn(false);

        mockMvc.perform(delete("/rule/{id}", ruleId))
                .andExpect(status().isNotFound()); // Check that the response indicates not found
    }
}
