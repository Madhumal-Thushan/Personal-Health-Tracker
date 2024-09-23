package com.example.Personal.Health.Tracker.Dto;

import com.example.Personal.Health.Tracker.Enum.GoalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HealthGoalDto {

    private Long userId;
    private GoalType goalType;
    private Double targetValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isActive;
    private Boolean achieved;
}
