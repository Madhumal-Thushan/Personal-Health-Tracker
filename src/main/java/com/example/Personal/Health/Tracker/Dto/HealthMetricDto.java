package com.example.Personal.Health.Tracker.Dto;

import com.example.Personal.Health.Tracker.Entity.Users;
import lombok.Data;

import java.util.Date;

@Data
public class HealthMetricDto {
    private Long id;
    private Users userId;
    private Double weight;
    private Double height;
    private Double calories;
    private Double steps;
    private Float value;
    private Date updatedAt;
}
