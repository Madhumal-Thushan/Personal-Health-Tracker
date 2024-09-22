package com.example.Personal.Health.Tracker.Dto.Response;

import com.example.Personal.Health.Tracker.Dto.UserDto;
import com.example.Personal.Health.Tracker.Enum.GoalType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class GoalResponse {
    private Long id;
    private GoalType goalType;
    private double targetValue;
    private boolean achieved;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    private Boolean isActive;
    private UserDto user;
}
