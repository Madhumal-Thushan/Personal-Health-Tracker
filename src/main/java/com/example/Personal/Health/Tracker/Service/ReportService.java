package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Dto.HealthGoalDto;
import com.example.Personal.Health.Tracker.Entity.Goal;
import com.example.Personal.Health.Tracker.Entity.HealthMetric;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportService {

    @Autowired
    private HealthMetricService healthMetricService;

    @Autowired
    private HealthGoalService goalService;

    /**
     * Generate Weekly Reports
     */
    public String generateWeeklyReport(Long userId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);

        List<HealthMetric> weeklyMetrics = healthMetricService.getHealthMetricsByDateRange(userId,startDate,endDate);
        List<Goal> goals = goalService.geGoalByUser(userId);

        return createReport(weeklyMetrics,goals,"Weekly");
    }

    public String generateMonthlyReport(Long userId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);

        List<HealthMetric> weeklyMetrics = healthMetricService.getHealthMetricsByDateRange(userId,startDate,endDate);
        List<Goal> goals = goalService.geGoalByUser(userId);

        return createReport(weeklyMetrics,goals,"Monthly");
    }

    private String createReport(List<HealthMetric> metrics, List<Goal> goals, String reportType) {

        StringBuilder report = new StringBuilder();
        report.append(reportType).append("Progress Report \n");

        report.append("Health Metrics \n");
        for(HealthMetric metric: metrics){
            report.append(metric.getMetricType()).append(":").append(metric.getValue()).append(" ").append("on").append(" ")
                    .append(metric.getCreatedDate()).append("\n");
        }
        report.append("Goals : \n");
        for(Goal goal : goals){
            report.append(goal.getGoalType()).append(" ")
                    .append(": Target ")
                    .append(goal.getTargetValue()).append(" ").append("Achieved")
                    .append("\n");
        }
        return report.toString();
    }
}
