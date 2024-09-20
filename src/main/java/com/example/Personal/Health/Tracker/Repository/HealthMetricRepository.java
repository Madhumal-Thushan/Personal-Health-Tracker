package com.example.Personal.Health.Tracker.Repository;

import com.example.Personal.Health.Tracker.Entity.HealthMetric;
import com.example.Personal.Health.Tracker.Enum.MetricType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HealthMetricRepository extends JpaRepository<HealthMetric,Long> {

    List<HealthMetric> findByUserId(Long userId);
    List<HealthMetric> findByUserIdAndMetricType(Long id, MetricType metricType);

    List<HealthMetric> findByUserIdAndCreatedDateBetween(Long id, LocalDate startDate,LocalDate endDate);

}
