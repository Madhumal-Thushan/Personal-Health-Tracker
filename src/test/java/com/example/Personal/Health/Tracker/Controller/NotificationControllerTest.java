package com.example.Personal.Health.Tracker.Controller;

import com.example.Personal.Health.Tracker.Service.Notification.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void sendGoalAchievementNotification_ShouldReturnSuccess() {
        Long userId = 1L;

        // Mocking the service call
        when(notificationService.checkGoalAchievementAndSendNotification(userId)).thenReturn(true);

        ResponseEntity<String> responseEntity = notificationController.sendGoalAchievementNotification(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Goal Achievement Notification Sent Successfully", responseEntity.getBody());

        verify(notificationService, times(1)).checkGoalAchievementAndSendNotification(userId);
    }

    @Test
    void sendGoalAchievementNotification_ShouldReturnNotAchieved() {
        Long userId = 1L;

        // Mocking the service call to return false
        when(notificationService.checkGoalAchievementAndSendNotification(userId)).thenReturn(false);

        ResponseEntity<String> responseEntity = notificationController.sendGoalAchievementNotification(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Goal not Achieved not yet, No Notification sent", responseEntity.getBody());

        verify(notificationService, times(1)).checkGoalAchievementAndSendNotification(userId);
    }

    @Test
    void sendGoalAchievementNotification_ShouldReturnError() {
        Long userId = 1L;

        // Mocking the service call to throw an exception
        when(notificationService.checkGoalAchievementAndSendNotification(userId)).thenThrow(new RuntimeException("Service not available"));

        ResponseEntity<String> responseEntity = notificationController.sendGoalAchievementNotification(userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Failed to send Notification"));

        verify(notificationService, times(1)).checkGoalAchievementAndSendNotification(userId);
    }

    @Test
    void sendGoalReminderNotification_ShouldReturnSuccess() {
        Long userId = 1L;

        // Mocking the service call
        when(notificationService.sendGoalReminderNotification(userId)).thenReturn(true);

        ResponseEntity<String> responseEntity = notificationController.sendGoalReminderNotification(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("Goal  Reminder Sent Successfully", responseEntity.getBody());

        verify(notificationService, times(1)).sendGoalReminderNotification(userId);
    }

    @Test
    void sendGoalReminderNotification_ShouldReturnNoReminderNeeded() {
        Long userId = 1L;

        // Mocking the service call to return false
        when(notificationService.sendGoalReminderNotification(userId)).thenReturn(false);

        ResponseEntity<String> responseEntity = notificationController.sendGoalReminderNotification(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("No Reminder Needed", responseEntity.getBody());

        verify(notificationService, times(1)).sendGoalReminderNotification(userId);
    }

    @Test
    void sendGoalReminderNotification_ShouldReturnError() {
        Long userId = 1L;

        // Mocking the service call to throw an exception
        when(notificationService.sendGoalReminderNotification(userId)).thenThrow(new RuntimeException("Service not available"));

        ResponseEntity<String> responseEntity = notificationController.sendGoalReminderNotification(userId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseEntity.getStatusCode());
        assertTrue(responseEntity.getBody().contains("Failed to send Reminder"));

        verify(notificationService, times(1)).sendGoalReminderNotification(userId);
    }
}
