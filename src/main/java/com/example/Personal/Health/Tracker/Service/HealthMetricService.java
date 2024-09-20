package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Dto.HealthMetricDto;
import com.example.Personal.Health.Tracker.Entity.HealthMetric;
import com.example.Personal.Health.Tracker.Exception.ResourceNotFoundException;
import com.example.Personal.Health.Tracker.Repository.HealthMetricRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class HealthMetricService {

    @Autowired
    private HealthMetricRepository healthMetricRepository;

    @Transactional
    public HealthMetric addHealthMetrics(HealthMetric healthMetric) {
        return healthMetricRepository.save(healthMetric);
    }
    @Transactional
    public List<HealthMetricDto> getHealthMetricsByUser(Long userId) {
        List<HealthMetric> healthMetricList = healthMetricRepository.findByUserId(userId);
        if(healthMetricList.isEmpty() ) {
            throw new ResourceNotFoundException("No Health Metrics Found for user ID " +userId);
        }
        return healthMetricList.stream()
                .map(this::convertToDto)
                .toList();
    }

    @Transactional
    public HealthMetricDto getHealthMetricById(Long id) {
        HealthMetric metric = healthMetricRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("HealthMetric not found with id " + id));
        return convertToDto(metric);
    }
    @Transactional
    public HealthMetricDto updateHealthMetric(Long id, HealthMetric metric) {
        Optional<HealthMetric> healthMetricOpt = healthMetricRepository.findById(id);
        if(healthMetricOpt.isPresent() ) {
            HealthMetric healthMetric = healthMetricOpt.get();
            healthMetric.setId(healthMetric.getId());
            healthMetric.setMetricType(metric.getMetricType());
            healthMetric.setValue(metric.getValue());
            healthMetric.setUpdatedDate(LocalDate.now());
            healthMetric.setUser(healthMetricOpt.get().getUser());
            healthMetricRepository.save(healthMetric);
        }
        return convertToDto(metric);
    }

    private HealthMetricDto convertToDto(HealthMetric metric) {
        HealthMetricDto dto = new HealthMetricDto();
        dto.setId(metric.getId());
        dto.setMetricType(metric.getMetricType());
        dto.setValue(metric.getValue());
        dto.setUpdatedDate(metric.getUpdatedDate());
        return dto;
    }
}
