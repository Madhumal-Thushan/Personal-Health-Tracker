package com.example.Personal.Health.Tracker.Dto;

import lombok.Data;

@Data
public class WeeklyReportDto {


    private Double averageSteps;
    private Double averageCalories;
    private Double weightChange;


    public WeeklyReportDto(Double averageSteps, Double averageCalories, Double weightChange) {
        this.averageSteps = averageSteps;
        this.averageCalories = averageCalories;
        this.weightChange = weightChange;
    }
}
