package com.example.Personal.Health.Tracker.Dto;

import com.example.Personal.Health.Tracker.Enum.MetricType;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthMetricDto {

    private Long userId;
    private MetricType metricType;
    private Double value;
    private LocalDate updatedDate;
    private LocalDate createdDate;

}
