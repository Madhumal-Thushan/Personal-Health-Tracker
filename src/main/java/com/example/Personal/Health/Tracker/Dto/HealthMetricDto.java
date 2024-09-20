package com.example.Personal.Health.Tracker.Dto;

import com.example.Personal.Health.Tracker.Enum.MetricType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthMetricDto {
    private Long id;
    private MetricType metricType;
    private Float value;
    private LocalDate updatedDate;
    private LocalDate createdDate;

}
