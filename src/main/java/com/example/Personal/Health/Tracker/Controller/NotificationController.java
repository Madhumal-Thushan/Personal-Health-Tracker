package com.example.Personal.Health.Tracker.Controller;

import com.example.Personal.Health.Tracker.Service.Notification.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send-goal-achievement/")
    public ResponseEntity<String> sendGoalAchievementNotification(@RequestParam Long userId) {
        try {
            Boolean notificationSent = notificationService.checkGoalAchievementAndSendNotification(userId);
            if(notificationSent){
                return new ResponseEntity<>("Goal Achievement Notification Sent Successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Goal not Achieved not yet, No Notification sent", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to send Notification"+ e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/send-goal-reminder/")
    public ResponseEntity<String> sendGoalReminderNotification(@RequestParam Long userId) {
        try {
            Boolean reminderSent = notificationService.sendGoalReminderNotification(userId);
            if(reminderSent){
                return new ResponseEntity<>("Goal  Reminder Sent Successfully", HttpStatus.OK);

            }else {
                return new ResponseEntity<>("No Reminder Needed", HttpStatus.OK);
            }
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to send Reminder" + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
