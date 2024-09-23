package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Entity.Goal;
import com.example.Personal.Health.Tracker.Entity.HealthMetric;
import com.example.Personal.Health.Tracker.Entity.Users;
import com.example.Personal.Health.Tracker.Enum.GoalType;
import com.example.Personal.Health.Tracker.Enum.MetricType;
import com.example.Personal.Health.Tracker.Exception.ResourceNotFoundException;
import com.example.Personal.Health.Tracker.Repository.HealthGoalRepository;
import com.example.Personal.Health.Tracker.Repository.HealthMetricRepository;
import com.example.Personal.Health.Tracker.Repository.UserRepository;
import com.example.Personal.Health.Tracker.Service.Notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class NotificationServiceTest {

    @Mock
    private HealthGoalRepository goalRepository;

    @Mock
    private HealthMetricRepository metricRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender mailSender;

    @InjectMocks
    private NotificationService notificationService;

    private Users user;
    private Goal goal;
    private HealthMetric healthMetric;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new Users();
        user.setId(1L);
        user.setEmail("testuser@example.com");

        goal = new Goal();
        goal.setId(1L);
        goal.setGoalType(GoalType.Weight_Goal);
        goal.setTargetValue(75.0);
        goal.setEndDate(LocalDate.now().plusDays(7));
        goal.setIsActive(true);

        healthMetric = new HealthMetric();
        healthMetric.setValue(70.0);
    }

    @Test
    void checkGoalAchievementAndSendNotification_GoalAchieved() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(goalRepository.findByUsersIdAndIsActiveTrue(1L)).thenReturn(Collections.singletonList(goal));
        when(metricRepository.findTopByUserIdAndMetricTypeOrderByCreatedDateDesc(1L, MetricType.Weight)).thenReturn(healthMetric);

        Boolean result = notificationService.checkGoalAchievementAndSendNotification(1L);

        assertTrue(result);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void checkGoalAchievementAndSendNotification_GoalNotAchieved() {
        healthMetric.setValue(80.0);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(goalRepository.findByUsersIdAndIsActiveTrue(1L)).thenReturn(Collections.singletonList(goal));
        when(metricRepository.findTopByUserIdAndMetricTypeOrderByCreatedDateDesc(1L, MetricType.Weight)).thenReturn(healthMetric);

        Boolean result = notificationService.checkGoalAchievementAndSendNotification(1L);

        assertFalse(result);
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void checkGoalAchievementAndSendNotification_NoGoalsFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(goalRepository.findByUsersIdAndIsActiveTrue(1L)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.checkGoalAchievementAndSendNotification(1L);
        });

        assertEquals("No Goals Found for user :1", exception.getMessage());
    }

    @Test
    void sendGoalReminderNotification_ReminderNeeded() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(goalRepository.findByUsersIdAndIsActiveTrue(1L)).thenReturn(Collections.singletonList(goal));

        when(metricRepository.findTopByUserIdAndMetricTypeOrderByCreatedDateDesc(1L, MetricType.Weight)).thenReturn(healthMetric);

        when(notificationService.isReminderNeeded(1L)).thenReturn(true);

        Boolean result = notificationService.sendGoalReminderNotification(1L);

        assertTrue(result);
        verify(mailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendGoalReminderNotification_NoReminderNeeded() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        when(goalRepository.findByUsersIdAndIsActiveTrue(1L)).thenReturn(Collections.singletonList(goal));
        when(metricRepository.findTopByUserIdAndMetricTypeOrderByCreatedDateDesc(1L, MetricType.Weight)).thenReturn(healthMetric);

        when(notificationService.isReminderNeeded(1L)).thenReturn(false);

        Boolean result = notificationService.sendGoalReminderNotification(1L);

        assertFalse(result);
        verify(mailSender, never()).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendGoalReminderNotification_NoGoalsFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(goalRepository.findByUsersIdAndIsActiveTrue(1L)).thenReturn(Collections.emptyList());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            notificationService.sendGoalReminderNotification(1L);
        });

        assertEquals("No Goals Found for user :1", exception.getMessage());
    }
}