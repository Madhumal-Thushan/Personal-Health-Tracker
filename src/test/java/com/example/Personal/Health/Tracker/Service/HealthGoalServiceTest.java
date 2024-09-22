package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Dto.HealthGoalDto;
import com.example.Personal.Health.Tracker.Dto.HealthGoalUpdateDto;
import com.example.Personal.Health.Tracker.Dto.Response.GoalResponse;
import com.example.Personal.Health.Tracker.Dto.Response.HealthMetricResponse;
import com.example.Personal.Health.Tracker.Entity.Goal;
import com.example.Personal.Health.Tracker.Entity.Users;
import com.example.Personal.Health.Tracker.Enum.GoalType;
import com.example.Personal.Health.Tracker.Exception.ResourceNotFoundException;
import com.example.Personal.Health.Tracker.Repository.HealthGoalRepository;
import com.example.Personal.Health.Tracker.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class HealthGoalServiceTest {

    @Mock
    private HealthGoalRepository healthGoalRepository;

    @Mock
    private HealthMetricService healthMetricService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private HealthGoalService healthGoalService;

    private Users user;
    private Goal goal;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new Users();
        user.setId(1L);
        user.setUsername("testUser");

        goal = new Goal();
        goal.setId(1L);
        goal.setUsers(user);
        goal.setGoalType(GoalType.Weight_Goal);
        goal.setTargetValue(70.0);
        goal.setCreatedAt(LocalDate.now());
        goal.setStartDate(LocalDate.now());
        goal.setEndDate(LocalDate.now().plusDays(30));
        goal.setAchieved(false);
        goal.setIsActive(true);
    }

    @Test
    void setHealthGoal_ShouldSaveGoal() {
        HealthGoalDto dto = new HealthGoalDto();
        dto.setUserId(1L);
        dto.setGoalType(GoalType.Weight_Goal);
        dto.setTargetValue(70.0);
        dto.setStartDate(LocalDate.now());
        dto.setEndDate(LocalDate.now().plusDays(30));

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(healthGoalRepository.save(any(Goal.class))).thenReturn(goal);

        GoalResponse response = healthGoalService.setHealthGoal(dto);

        assertNotNull(response);
        assertEquals(goal.getId(), response.getId());
        assertEquals(goal.getGoalType(), response.getGoalType());
        assertEquals(goal.getTargetValue(), response.getTargetValue());

        verify(userRepository).findById(1L);
        verify(healthGoalRepository).save(any(Goal.class));
    }

    @Test
    void setHealthGoal_UserNotFound_ShouldThrowException() {
        HealthGoalDto dto = new HealthGoalDto();
        dto.setUserId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            healthGoalService.setHealthGoal(dto);
        });

        assertEquals("User not found" + dto.getUserId(), exception.getMessage());
    }

    @Test
    void getHealthGoalByUser_ShouldReturnGoals() {
        when(healthGoalRepository.findByUsersId(1L)).thenReturn(Collections.singletonList(goal));

        List<GoalResponse> response = healthGoalService.getHealthGoalByUser(1L);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertEquals(goal.getId(), response.get(0).getId());

        verify(healthGoalRepository).findByUsersId(1L);
    }

    @Test
    void getHealthGoalByUser_NoGoals_ShouldThrowException() {
        when(healthGoalRepository.findByUsersId(1L)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            healthGoalService.getHealthGoalByUser(1L);
        });

        assertEquals("Health Goal not found for user ID : 1", exception.getMessage());
    }

    @Test
    void updateHealthGoal_ShouldUpdateGoal() {
        HealthGoalUpdateDto updateDto = new HealthGoalUpdateDto();
        updateDto.setTargetValue(65.0);
        updateDto.setIsActive(true);
        updateDto.setUserId(1L);

        when(healthGoalRepository.findById(1L)).thenReturn(Optional.of(goal));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(healthGoalRepository.save(any(Goal.class))).thenReturn(goal);

        GoalResponse response = healthGoalService.updateHealthGoal(1L, updateDto);

        assertNotNull(response);
        assertEquals(65.0, response.getTargetValue());
        assertTrue(goal.getIsActive());

        verify(healthGoalRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(healthGoalRepository).save(any(Goal.class));
    }

    @Test
    void updateHealthGoal_GoalNotFound_ShouldThrowException() {
        HealthGoalUpdateDto updateDto = new HealthGoalUpdateDto();
        updateDto.setTargetValue(65.0);

        when(healthGoalRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            healthGoalService.updateHealthGoal(1L, updateDto);
        });

        assertEquals("Goal not found", exception.getMessage());
    }

    @Test
    void trackGoalProgress_NoGoalsFound_ShouldThrowException() {
        when(healthGoalRepository.findByUsersId(1L)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            healthGoalService.trackGoalProgress(1L);
        });

        assertEquals("No goals found for user: 1", exception.getMessage());
    }

    @Test
    void trackGoalProgress_ShouldTrackProgressAndAchieveGoal() {
        List<Goal> goals = new ArrayList<>();
        goals.add(goal);

        HealthMetricResponse healthMetric = new HealthMetricResponse();
        healthMetric.setValue(70.0); // Less than target for Weight_Goal

        when(healthGoalRepository.findByUsersId(1L)).thenReturn(goals);
        when(healthMetricService.getHealthMetricsByUser(1L)).thenReturn(Collections.singletonList(healthMetric));

        List<GoalResponse> response = healthGoalService.trackGoalProgress(1L);

        assertNotNull(response);
        assertEquals(1, response.size());
        verify(healthGoalRepository).save(any(Goal.class)); // Check that goal was saved
    }


    @Test
    void trackGoalProgress_NoHealthMetrics_ShouldReturnGoals() {
        List<Goal> goals = new ArrayList<>();
        goals.add(goal);

        when(healthGoalRepository.findByUsersId(1L)).thenReturn(goals);
        when(healthMetricService.getHealthMetricsByUser(1L)).thenReturn(Collections.emptyList());

        List<GoalResponse> response = healthGoalService.trackGoalProgress(1L);

        assertNotNull(response);
        assertEquals(1, response.size());
        assertFalse(response.get(0).isAchieved()); // Goal should not be marked as achieved
        verify(healthGoalRepository).save(any(Goal.class)); // Check that goal was saved
    }

    @Test
    void trackGoalProgress_ShouldHandleMultipleGoals() {
        Goal goal2 = new Goal();
        goal2.setId(2L);
        goal2.setGoalType(GoalType.Step_Goal);
        goal2.setTargetValue(10000.0);
        goal2.setAchieved(false);
        goal2.setIsActive(true);
        goal2.setStartDate(LocalDate.now());
        goal2.setEndDate(LocalDate.now().plusDays(30));

        List<Goal> goals = new ArrayList<>();
        goals.add(goal);
        goals.add(goal2);

        HealthMetricResponse healthMetric = new HealthMetricResponse();
        healthMetric.setValue(5000.0); // Less than target for Step_Goal

        when(healthGoalRepository.findByUsersId(1L)).thenReturn(goals);
        when(healthMetricService.getHealthMetricsByUser(1L)).thenReturn(Collections.singletonList(healthMetric));

        List<GoalResponse> response = healthGoalService.trackGoalProgress(1L);

        assertNotNull(response);
        assertEquals(2, response.size());
        assertFalse(response.get(1).isAchieved()); // Step goal not achieved

        verify(healthGoalRepository, times(2)).save(any(Goal.class)); // Check that both goals were saved
    }
}
