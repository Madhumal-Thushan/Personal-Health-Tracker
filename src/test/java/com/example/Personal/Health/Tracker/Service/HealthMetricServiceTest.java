package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Dto.HealthMetricDto;
import com.example.Personal.Health.Tracker.Dto.Response.HealthMetricResponse;
import com.example.Personal.Health.Tracker.Entity.HealthMetric;
import com.example.Personal.Health.Tracker.Entity.Users;
import com.example.Personal.Health.Tracker.Enum.MetricType;
import com.example.Personal.Health.Tracker.Exception.ResourceNotFoundException;
import com.example.Personal.Health.Tracker.Repository.HealthMetricRepository;
import com.example.Personal.Health.Tracker.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HealthMetricServiceTest {

    @Mock
    private HealthMetricRepository healthMetricRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private HealthMetricService healthMetricService;

    private Users user;
    private HealthMetric metric;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new Users();
        user.setId(1L);
        user.setUsername("testUser");

        metric = new HealthMetric();
        metric.setId(1L);
        metric.setMetricType(MetricType.Weight);
        metric.setValue(70.0);
        metric.setCreatedDate(LocalDate.now());
        metric.setUser(user);
    }

    @Test
    void addHealthMetrics_ShouldSaveMetric() {
        HealthMetricDto dto = new HealthMetricDto();
        dto.setUserId(1L);
        dto.setMetricType(MetricType.Weight);
        dto.setValue(70.0);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(healthMetricRepository.save(any(HealthMetric.class))).thenReturn(metric);

        HealthMetricResponse response = healthMetricService.addHealthMetrics(dto);

        assertNotNull(response);
        assertEquals(metric.getId(), response.getId());
        assertEquals(metric.getMetricType(), response.getMetricType());
        assertEquals(metric.getValue(), response.getValue());

        verify(userRepository).findById(1L);
        verify(healthMetricRepository).save(any(HealthMetric.class));
    }

    @Test
    void addHealthMetrics_UserNotFound_ShouldThrowException() {
        HealthMetricDto dto = new HealthMetricDto();
        dto.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            healthMetricService.addHealthMetrics(dto);
        });

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getHealthMetricsByUser_ShouldReturnMetrics() {
        when(healthMetricRepository.findByUserId(1L)).thenReturn(Collections.singletonList(metric));

        List<HealthMetricResponse> response = healthMetricService.getHealthMetricsByUser(1L);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(metric.getId(), response.get(0).getId());

        verify(healthMetricRepository).findByUserId(1L);
    }

    @Test
    void getHealthMetricsByUser_NoMetrics_ShouldThrowException() {
        when(healthMetricRepository.findByUserId(1L)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            healthMetricService.getHealthMetricsByUser(1L);
        });

        assertEquals("No Health Metrics Found for user ID 1", exception.getMessage());
    }

    @Test
    void getHealthMetricById_ShouldReturnMetric() {
        when(healthMetricRepository.findById(1L)).thenReturn(Optional.of(metric));

        HealthMetricResponse response = healthMetricService.getHealthMetricById(1L);

        assertNotNull(response);
        assertEquals(metric.getId(), response.getId());

        verify(healthMetricRepository).findById(1L);
    }

    @Test
    void getHealthMetricById_MetricNotFound_ShouldThrowException() {
        when(healthMetricRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            healthMetricService.getHealthMetricById(1L);
        });

        assertEquals("HealthMetric not found with id 1", exception.getMessage());
    }

    @Test
    void updateHealthMetric_ShouldUpdateMetric() {
        HealthMetricDto updateDto = new HealthMetricDto();
        updateDto.setMetricType(MetricType.Weight);
        updateDto.setValue(65.0);

        when(healthMetricRepository.findById(1L)).thenReturn(Optional.of(metric));
        when(healthMetricRepository.save(any(HealthMetric.class))).thenReturn(metric);

        HealthMetricResponse response = healthMetricService.updateHealthMetric(1L, updateDto);

        assertNotNull(response);
        assertEquals(65.0, response.getValue());

        verify(healthMetricRepository).findById(1L);
        verify(healthMetricRepository).save(any(HealthMetric.class));
    }

    @Test
    void updateHealthMetric_MetricNotFound_ShouldThrowException() {
        HealthMetricDto updateDto = new HealthMetricDto();
        updateDto.setMetricType(MetricType.Weight);
        updateDto.setValue(65.0);

        when(healthMetricRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            healthMetricService.updateHealthMetric(1L, updateDto);
        });

        assertEquals("HealthMetric not found with id 1", exception.getMessage());
    }
}