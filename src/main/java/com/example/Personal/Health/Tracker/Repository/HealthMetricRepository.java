package com.example.Personal.Health.Tracker.Repository;

import com.example.Personal.Health.Tracker.Dto.MonthlyReportDto;
import com.example.Personal.Health.Tracker.Dto.WeeklyReportDto;
import com.example.Personal.Health.Tracker.Entity.HealthMetric;
import com.example.Personal.Health.Tracker.Enum.MetricType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface HealthMetricRepository extends JpaRepository<HealthMetric,Long> {

    List<HealthMetric> findByUserId(Long userId);
    List<HealthMetric> findByUserIdAndMetricType(Long id, MetricType metricType);

    List<HealthMetric> findByUserIdAndCreatedDateBetween(Long id, LocalDate startDate,LocalDate endDate);

    @Query("SELECT new com.example.Personal.Health.Tracker.Dto.WeeklyReportDto(" +
            "(SELECT AVG(h.value) FROM HealthMetric h WHERE h.user.id = :userId AND h.metricType = 'Steps' AND h.createdDate BETWEEN :startDate AND :endDate), " +
            "(SELECT AVG(h.value) FROM HealthMetric h WHERE h.user.id = :userId AND h.metricType = 'Calories' AND h.createdDate BETWEEN :startDate AND :endDate), " +
            "(SELECT MAX(h.value) - MIN(h.value) FROM HealthMetric h WHERE h.user.id = :userId AND h.metricType = 'Weight' AND h.createdDate BETWEEN :startDate AND :endDate))")
    WeeklyReportDto getWeeklyAggregatedData(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query("SELECT new com.example.Personal.Health.Tracker.Dto.MonthlyReportDto(" +
            "(SELECT AVG(h.value) FROM HealthMetric h WHERE h.user.id = :userId AND h.metricType = 'Steps' AND h.createdDate BETWEEN :startDate AND :endDate), " +
            "(SELECT AVG(h.value) FROM HealthMetric h WHERE h.user.id = :userId AND h.metricType = 'Calories' AND h.createdDate BETWEEN :startDate AND :endDate), " +
            "(SELECT MAX(h.value) - MIN(h.value) FROM HealthMetric h WHERE h.user.id = :userId AND h.metricType = 'Weight' AND h.createdDate BETWEEN :startDate AND :endDate))")
    MonthlyReportDto getMonthlyAggregatedData(@Param("userId") Long userId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT h FROM HealthMetric h WHERE h.user.id = :userId AND h.metricType = :metricType")
    List<HealthMetric> findMetricsForGoal(@Param("userId") Long userId,@Param("metricType") MetricType metricType);

    HealthMetric findTopByUserIdAndMetricTypeOrderByCreatedDateDesc(Long userId, MetricType metricType);
}
