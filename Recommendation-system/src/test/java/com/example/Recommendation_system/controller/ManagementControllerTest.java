package com.example.Recommendation_system.controller;

import org.glassfish.jersey.internal.util.collection.Cache;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Map;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableConfigurationProperties
public class ManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private Cache<String, Boolean> queryResultCache;

    @Mock
    private BuildProperties buildProperties;

    @InjectMocks
    private ManagementController managementController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        managementController = new ManagementController((com.github.benmanes.caffeine.cache.Cache<String, Boolean>) queryResultCache, buildProperties);
    }

    @Test
    public void clearCaches_ShouldReturnSuccessResponse() throws Exception {
        // Given
        doNothing().when(queryResultCache);

        // When and Then
        mockMvc.perform(post("/management/clear-caches")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(jsonPath("$.status").value("Cache cleared")); // Verify response content
    }

    @Test
    public void getServiceInfo_ShouldReturnServiceInfo() throws Exception {
        // Given
        when(buildProperties.getName()).thenReturn("Test Service");
        when(buildProperties.getVersion()).thenReturn("1.0.0");

        // When and Then
        mockMvc.perform(get("/management/info")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // Expect HTTP 200
                .andExpect(jsonPath("$.name").value("Test Service")) // Verify name
                .andExpect(jsonPath("$.version").value("1.0.0")); // Verify version
    }
}
