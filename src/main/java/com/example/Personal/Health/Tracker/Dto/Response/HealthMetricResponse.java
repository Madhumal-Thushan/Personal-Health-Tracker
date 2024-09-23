package com.example.Personal.Health.Tracker.Dto.Response;

import com.example.Personal.Health.Tracker.Dto.UserDto;
import com.example.Personal.Health.Tracker.Enum.MetricType;
import lombok.Data;

import java.time.LocalDate;


@Data
public class HealthMetricResponse {
    private Long id;
    private MetricType metricType;
    private double value;
    private LocalDate createdDate;
    private UserDto user;
}
