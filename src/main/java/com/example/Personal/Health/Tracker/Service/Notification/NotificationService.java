package com.example.Personal.Health.Tracker.Service.Notification;

import com.example.Personal.Health.Tracker.Entity.Goal;
import com.example.Personal.Health.Tracker.Entity.HealthMetric;
import com.example.Personal.Health.Tracker.Entity.Users;
import com.example.Personal.Health.Tracker.Enum.GoalType;
import com.example.Personal.Health.Tracker.Enum.MetricType;
import com.example.Personal.Health.Tracker.Exception.ResourceNotFoundException;
import com.example.Personal.Health.Tracker.Repository.HealthGoalRepository;
import com.example.Personal.Health.Tracker.Repository.HealthMetricRepository;
import com.example.Personal.Health.Tracker.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NotificationService {
    @Autowired
    private HealthGoalRepository goalRepository;

    @Autowired
    private HealthMetricRepository metricRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    /**
     * Shceduled Task runs Every day 8 AM to Send Notifications and Reminders to All Users
     */
    @Async
    @Scheduled(cron = "0 0 8 * * ?")
//    @Scheduled(cron = "0 * * * * ?") // to test this method Run every 1 minute
    public void checkAndSendGoalAchievementNotification() {
        //get all users
        List<Long> userIds = getAllUserIds();
        for(Long userId : userIds) {
            checkGoalAchievementAndSendNotification(userId);
            System.out.println("Notification sent");

        }
    }
    @Async
    @Scheduled(cron = "0 0 8 * * ?")
//    @Scheduled(cron = "0 * * * * ?") // to test this method Run every 1 minute
    public void checkAndSendGoalReminders() {
        //get all users
        List<Long> userIds = getAllUserIds();
        for(Long userId : userIds) {
            sendGoalReminderNotification(userId);
            System.out.println("Reminder sent");
        }
    }

    private List<Long> getAllUserIds() {
        List<Users> users = userRepository.findAll();
        if(!users.isEmpty()){
            return users.stream().map(Users::getId).collect(Collectors.toList());
        }
        throw new  ResourceNotFoundException("No users Found");
    }

    public Boolean checkGoalAchievementAndSendNotification(Long userId) {
        Boolean goalAchieved = isGoalAchieved(userId);
        if(goalAchieved) {
            String userEmail = getUserEmail(userId);
            String subject = "Congratulations! You've achieved your goal!";
            String message = "You have successfully reached your goal, Keep up the good work!";
            sendEmailNotification(userEmail, subject, message);
            return  true;
        }
        return false;
    }

    /**
     * Send Reminder Notifications
     * @param userId
     * @return boolean
     */
    public Boolean sendGoalReminderNotification(Long userId) {
        Boolean reminderNeeded = isReminderNeeded(userId);
        if(reminderNeeded) {
            String userEmail = getUserEmail(userId);
            String subject = "Reminder: Keep working towards your goal!";
            String message = "You are close to reaching your goal Keep going!";
            sendEmailNotification(userEmail, subject, message);
            return true;
        }
        return false;
    }

    private String getUserEmail(Long userId) {
        Optional<Users> usersOpt = userRepository.findById(userId);
        if(usersOpt.isPresent()){
            return usersOpt.get().getEmail();
        }
        throw new ResourceNotFoundException("User Not Found for user ID" +userId);
    }

    /**
     * JAava MailSender, to set the Mail Attributes
     * @param to
     * @param subject
     * @param message
     */
    @Async
    public void sendEmailNotification(String to, String subject,String message) {
        SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(to);
        email.setSubject(subject);
        email.setText(message);
        mailSender.send(email);
    }

    /**
     * TO check the Goals of the User is achieved or not
     * @param userId
     * @return
     */
    private Boolean isGoalAchieved(Long userId){
        //get All Active goals For user
        List<Goal> goals = goalRepository.findByUsersIdAndIsActiveTrue(userId);
        if(goals.isEmpty()){
            throw new ResourceNotFoundException("No Goals Found for user :" +userId);
        }
        //go through all goals and check if they are Achieved
        for(Goal goal: goals) {
            GoalType goalType = goal.getGoalType();
            double targetValue = goal.getTargetValue();
            MetricType metricType = convertGoalTypeToMetricType(goalType);

            //get the latest metric value for aligned metricType
            HealthMetric healthMetric = metricRepository.findTopByUserIdAndMetricTypeOrderByCreatedDateDesc(userId,metricType);
            if(healthMetric!=null) {
                double currentValue = healthMetric.getValue();
                switch (goalType){
                    case Weight_Goal, Calories_Goal:
                        if(currentValue <= targetValue){
                            return true;
                        }
                        break;
                    case Step_Goal:
                        if(currentValue >= targetValue) {
                            return true;
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return false;
    }

    /**
     * Check Reminder needed Goals from users Goals
     * @param userId
     * @return
     */
    public Boolean isReminderNeeded(Long userId) {
        //get All Active goals For user
        List<Goal> goals = goalRepository.findByUsersIdAndIsActiveTrue(userId);
        if(goals.isEmpty()){
            throw new ResourceNotFoundException("No Goals Found for user :" +userId);
        }
        //go through all goals and check if they are Achieved
        for(Goal goal: goals) {
            GoalType goalType = goal.getGoalType();
            double targetValue = goal.getTargetValue();
            LocalDate targetDate = goal.getEndDate();
            MetricType metricType = convertGoalTypeToMetricType(goalType);

            //get the latest metric value for aligned metricType
            HealthMetric healthMetric = metricRepository.findTopByUserIdAndMetricTypeOrderByCreatedDateDesc(userId,metricType);
            if(healthMetric!=null) {
                double currentValue = healthMetric.getValue();
                switch (goalType){
                    case Weight_Goal, Calories_Goal:
                        if(currentValue <= targetValue){
                            return true;
                        }
                        break;
                    case Step_Goal:
                        if(currentValue >= targetValue) {
                            return true;
                        }
                        break;
                    default:
                        break;
                }

                //check if the Target date is withing 7 days and Goal is not achieved yet
                if(ChronoUnit.DAYS.between(LocalDate.now(),targetDate) <= 7){
                    if(goalType == GoalType.Weight_Goal && currentValue > targetValue) {
                        return true;
                    } else if(goalType == GoalType.Step_Goal && currentValue < targetValue){
                        return true;
                    } else if(goalType == GoalType.Calories_Goal && currentValue > targetValue){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This private method used to Convert Goal Type to Metric Type
     * @param goalType
     * @return
     */
    private MetricType convertGoalTypeToMetricType(GoalType goalType){
        return switch (goalType) {
            case Weight_Goal -> MetricType.Weight;
            case Calories_Goal -> MetricType.Calories;
            case Step_Goal -> MetricType.Steps;
            default -> throw new IllegalArgumentException("Unknown Goal Type :" + goalType);
        };
    }

}
