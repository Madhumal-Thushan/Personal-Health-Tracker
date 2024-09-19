package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Dto.HealthMetricDto;
import com.example.Personal.Health.Tracker.Entity.HealthMetric;
import com.example.Personal.Health.Tracker.Exception.ResourceNotFoundException;
import com.example.Personal.Health.Tracker.Repository.HealthMetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Optional;

@Service
public class HealthMetricService {

    @Autowired
    private HealthMetricRepository healthMetricRepository;


    public HealthMetric addHealthMetrics(HealthMetric healthMetric) {
        return healthMetricRepository.save(healthMetric);
    }


    @Transactional
    public HealthMetric updateHealthMetric(Long id, HealthMetricDto updatedMetric) {
        HealthMetric healthMetric = healthMetricRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Metric not found with id " + id));
        if (healthMetric != null) {
            healthMetric.setUpdatedDate(new Date());
            healthMetric.setWeight(updatedMetric.getWeight());
            healthMetric.setHeight(updatedMetric.getHeight());
            healthMetric.setCalories(updatedMetric.getCalories());
            healthMetric.setSteps(updatedMetric.getSteps());
            return healthMetricRepository.save(healthMetric);
        }
        return null;
    }

    @Transactional
    public Optional<HealthMetric> getHealthMetricById(Long id) {
        Optional<HealthMetric> healthMetric = healthMetricRepository.findById(id);
        if (healthMetric == null) {
            throw new ResourceNotFoundException("Metric not found with id " + id);
        }
        return healthMetric;
    }
}
