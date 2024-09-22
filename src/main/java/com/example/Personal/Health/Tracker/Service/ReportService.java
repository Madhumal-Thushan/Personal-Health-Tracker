package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Dto.MonthlyReportDto;
import com.example.Personal.Health.Tracker.Dto.WeeklyReportDto;
import com.example.Personal.Health.Tracker.Exception.ResourceNotFoundException;
import com.example.Personal.Health.Tracker.Repository.HealthMetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Async
public class ReportService {

    @Autowired
    private HealthMetricService healthMetricService;

    @Autowired
    private HealthGoalService goalService;

    @Autowired
    private HealthMetricRepository healthMetricRepository;

    /**
     * Generate Average Weekly
     * @param userId
     * @return
     */
    @Transactional
    public WeeklyReportDto generateWeeklyReport(Long userId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusDays(7);

        //Get Average From Query
        WeeklyReportDto weeklyReport = healthMetricRepository.getWeeklyAggregatedData(userId,startDate,endDate);
        if(weeklyReport==null) {
            throw new ResourceNotFoundException("No Reports Found for user : " +userId);
        }
        return weeklyReport;

    }

    /**
     * Generate Average Monthly
     * @param userId
     * @return
     */
    @Transactional
    public MonthlyReportDto generateMonthlyReport(Long userId) {
        LocalDate endDate = LocalDate.now();
        LocalDate startDate = endDate.minusMonths(1);

        //Get Average From Query
        MonthlyReportDto monthlyReportDto = healthMetricRepository.getMonthlyAggregatedData(userId,startDate,endDate);
        if(monthlyReportDto==null) {
            throw new ResourceNotFoundException("No Reports Found for user : " +userId);
        }
        return monthlyReportDto;
    }

    //can Modify to user to Get Between Custom Dates
}
