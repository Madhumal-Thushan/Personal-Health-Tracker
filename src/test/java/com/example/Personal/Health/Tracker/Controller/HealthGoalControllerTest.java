package com.example.Personal.Health.Tracker.Controller;

import com.example.Personal.Health.Tracker.Dto.HealthGoalDto;
import com.example.Personal.Health.Tracker.Dto.HealthGoalUpdateDto;
import com.example.Personal.Health.Tracker.Dto.Response.GoalResponse;
import com.example.Personal.Health.Tracker.Service.HealthGoalService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HealthGoalControllerTest {

    @InjectMocks
    private HealthGoalController healthGoalController;

    @Mock
    private HealthGoalService healthGoalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateGoal() {
        HealthGoalDto goalDto = new HealthGoalDto();
        GoalResponse goalResponse = new GoalResponse();
        goalResponse.setId(1L);

        when(healthGoalService.setHealthGoal(any(HealthGoalDto.class))).thenReturn(goalResponse);

        ResponseEntity<GoalResponse> response = healthGoalController.createGoal(goalDto);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(goalResponse, response.getBody());
        verify(healthGoalService, times(1)).setHealthGoal(goalDto);
    }

    @Test
    void testGetHealthGoalByUser() {
        Long userId = 1L;
        GoalResponse goalResponse = new GoalResponse();
        goalResponse.setId(1L);
        List<GoalResponse> goals = Collections.singletonList(goalResponse);

        when(healthGoalService.getHealthGoalByUser(userId)).thenReturn(goals);

        ResponseEntity<List<GoalResponse>> response = healthGoalController.getHealthGoalByUser(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(goals, response.getBody());
        verify(healthGoalService, times(1)).getHealthGoalByUser(userId);
    }

    @Test
    void testUpdateHealthGoal() {
        Long goalId = 1L;
        HealthGoalUpdateDto updateDto = new HealthGoalUpdateDto();
        GoalResponse goalResponse = new GoalResponse();
        goalResponse.setId(goalId);

        when(healthGoalService.updateHealthGoal(goalId, updateDto)).thenReturn(goalResponse);

        ResponseEntity<GoalResponse> response = healthGoalController.updateHealthGoal(goalId, updateDto);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(goalResponse, response.getBody());
        verify(healthGoalService, times(1)).updateHealthGoal(goalId, updateDto);
    }

    @Test
    void testTrackProgress() {
        Long userId = 1L;
        GoalResponse goalResponse = new GoalResponse();
        goalResponse.setId(1L);
        List<GoalResponse> goals = Collections.singletonList(goalResponse);

        when(healthGoalService.trackGoalProgress(userId)).thenReturn(goals);

        ResponseEntity<List<GoalResponse>> response = healthGoalController.trackProgress(userId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(goals, response.getBody());
        verify(healthGoalService, times(1)).trackGoalProgress(userId);
    }
}