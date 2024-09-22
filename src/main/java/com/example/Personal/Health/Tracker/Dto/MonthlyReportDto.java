package com.example.Personal.Health.Tracker.Dto;

import lombok.Data;

@Data
public class MonthlyReportDto {


    private Double averageSteps;
    private Double averageCalories;
    private Double weightChange;


    public MonthlyReportDto(Double averageSteps, Double averageCalories, Double weightChange) {
        this.averageSteps = averageSteps;
        this.averageCalories = averageCalories;
        this.weightChange = weightChange;
    }

}
