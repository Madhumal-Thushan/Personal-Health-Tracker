package com.example.Personal.Health.Tracker.Controller;

import com.example.Personal.Health.Tracker.Dto.HealthMetricDto;
import com.example.Personal.Health.Tracker.Dto.Response.HealthMetricResponse;
import com.example.Personal.Health.Tracker.Service.HealthMetricService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class HealthMetricControllerTest {

    @InjectMocks
    private HealthMetricController healthMetricController;

    @Mock
    private HealthMetricService healthMetricService;

    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(healthMetricController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    void testAddHealthMetrics() throws Exception {
        HealthMetricDto requestDto = new HealthMetricDto();
        HealthMetricResponse response = new HealthMetricResponse();
        response.setId(1L);

        when(healthMetricService.addHealthMetrics(any(HealthMetricDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/metrics/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));

        verify(healthMetricService, times(1)).addHealthMetrics(any(HealthMetricDto.class));
    }

    @Test
    void testGetHealthMetricsByUser() throws Exception {
        Long userId = 1L;
        HealthMetricResponse response = new HealthMetricResponse();
        response.setId(1L);
        List<HealthMetricResponse> responses = Collections.singletonList(response);

        when(healthMetricService.getHealthMetricsByUser(userId)).thenReturn(responses);

        mockMvc.perform(get("/api/metrics/getAll")
                        .param("userId", String.valueOf(userId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));

        verify(healthMetricService, times(1)).getHealthMetricsByUser(userId);
    }

    @Test
    void testGetHealthMetricById() throws Exception {
        Long metricId = 1L;
        HealthMetricResponse response = new HealthMetricResponse();
        response.setId(metricId);

        when(healthMetricService.getHealthMetricById(metricId)).thenReturn(response);

        mockMvc.perform(get("/api/metrics/getHealthMetric")
                        .param("id", String.valueOf(metricId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(metricId));

        verify(healthMetricService, times(1)).getHealthMetricById(metricId);
    }

    @Test
    void testUpdateHealthMetric() throws Exception {
        Long metricId = 1L;
        HealthMetricDto metricDto = new HealthMetricDto();

        HealthMetricResponse response = new HealthMetricResponse();
        response.setId(metricId);

        when(healthMetricService.updateHealthMetric(eq(metricId), any(HealthMetricDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/metrics/edit")
                        .param("id", String.valueOf(metricId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(metricDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(metricId));

        verify(healthMetricService, times(1)).updateHealthMetric(eq(metricId), any(HealthMetricDto.class));
    }
}