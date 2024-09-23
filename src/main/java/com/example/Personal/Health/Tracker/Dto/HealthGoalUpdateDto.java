package com.example.Personal.Health.Tracker.Dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class HealthGoalUpdateDto {

    private Long userId;
    private Double targetValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
}
