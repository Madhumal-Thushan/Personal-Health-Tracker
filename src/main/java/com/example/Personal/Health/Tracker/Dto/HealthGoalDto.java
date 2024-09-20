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

    private Long id;
    private GoalType goalType;
    private Double targetValue;
    private LocalDate targetDate;
    private LocalDate createdDate;

    private Boolean achieved;
}
