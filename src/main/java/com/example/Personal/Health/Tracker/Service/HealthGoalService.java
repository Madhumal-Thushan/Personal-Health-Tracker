package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Dto.HealthGoalDto;
import com.example.Personal.Health.Tracker.Dto.HealthMetricDto;
import com.example.Personal.Health.Tracker.Entity.Goal;
import com.example.Personal.Health.Tracker.Entity.HealthMetric;
import com.example.Personal.Health.Tracker.Enum.GoalType;
import com.example.Personal.Health.Tracker.Enum.MetricType;
import com.example.Personal.Health.Tracker.Exception.ResourceNotFoundException;
import com.example.Personal.Health.Tracker.Repository.HealthGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class HealthGoalService {

    @Autowired
    private HealthGoalRepository healthGoalRepository;

    @Autowired
    private HealthMetricService healthMetricService;


    public HealthGoalDto setHealthGoal(Goal goal){
        healthGoalRepository.save(goal);
        return convertToDto(goal);
    }

    public List<HealthGoalDto> getHealthGoalByUser(Long userId) {
        List<Goal> goalList = healthGoalRepository.findByUsersId(userId);
        if(goalList.isEmpty()) {
            throw new ResourceNotFoundException("Health Goal not found for user ID : " +userId);
        }
        return goalList.stream()
                .map(this::convertToDto)
                .toList();
    }

    public List<Goal> geGoalByUser(Long userId) {
        List<Goal> goalList = healthGoalRepository.findByUsersId(userId);
        if(goalList.isEmpty()) {
            throw new ResourceNotFoundException("Health Goal not found for user ID : " +userId);
        }
        return goalList;
    }
    private HealthGoalDto convertToDto(Goal goal) {
        HealthGoalDto dto = new HealthGoalDto();
        dto.setId(goal.getId());
        dto.setGoalType(goal.getGoalType());
        dto.setCreatedDate(goal.getCreatedAt());
        dto.setTargetDate(goal.getTargetDate());
        dto.setUpdatedDate(goal.getUpdatedAt());
        dto.setAchieved(goal.getAchieved());
        return dto;
    }

    @Transactional
    public HealthGoalDto updateHealthGoal(Long id, Goal updateGoal) {
        HealthGoalDto dto = new HealthGoalDto();
        Goal existingGoal = healthGoalRepository.findById(id) .orElseThrow();

        existingGoal.setTargetValue(updateGoal.getTargetValue());
        existingGoal.setTargetDate(updateGoal.getTargetDate());
        existingGoal.setUpdatedAt(LocalDate.now());
        healthGoalRepository.save(existingGoal);

        dto.setId(existingGoal.getId());
        dto.setGoalType(existingGoal.getGoalType());
        dto.setTargetValue(existingGoal.getTargetValue());
        dto.setTargetDate(existingGoal.getTargetDate());
        dto.setAchieved(existingGoal.getAchieved());
        dto.setCreatedDate(existingGoal.getCreatedAt());
        return dto;
    }

    // Track progress towards a goal based on health metrics
    public List<HealthGoalDto> trackGoalProgress(Long userId) {
        List<Goal> goals = healthGoalRepository.findByUsersId(userId);
        List<HealthGoalDto> goalDtoList = new ArrayList<>();
        for (Goal goal : goals) {
            if (!goal.getAchieved()) {
                List<HealthMetric> healthMetricList = healthMetricService.getHealthMetricsByUserAndType(userId, convertGoalTypeToMetricType(goal.getGoalType()));
                double latestMetricValue = getLatestMetricValue(healthMetricList);
                if (goal.getGoalType() == GoalType.Weight_Goal && latestMetricValue <= goal.getTargetValue()) {
                    goal.setAchieved(true);
                } else if (goal.getGoalType() == GoalType.Step_Goal && latestMetricValue >= goal.getTargetValue()) {
                    goal.setAchieved(true); //Mark As Achieved if steps exceeded the Target
                } else if (goal.getGoalType() == GoalType.Calories_Goal && latestMetricValue <= goal.getTargetValue()) {
                    goal.setAchieved(true);
                }
                healthGoalRepository.save(goal);
            }
            HealthGoalDto dto = new HealthGoalDto();
            dto.setId(goal.getId());
            dto.setGoalType(goal.getGoalType());
            dto.setTargetValue(goal.getTargetValue());
            dto.setTargetDate(goal.getTargetDate());
            dto.setAchieved(goal.getAchieved());

            goalDtoList.add(dto);
        }
        return goalDtoList;
    }

    //Convert Goal type to Metric
    private MetricType convertGoalTypeToMetricType(GoalType goalType) {
        return switch (goalType) {
            case Weight_Goal -> MetricType.Weight;
            case Step_Goal -> MetricType.Steps;
            case Calories_Goal -> MetricType.Calories;
            default -> throw new IllegalArgumentException("Invalid Goal Type ");
        };
    }

    //Helper to get the Latest Metric Value
    private double getLatestMetricValue(List<HealthMetric> metrics) {
        if(!metrics.isEmpty()){
            HealthMetric latestMetric = metrics.get(metrics.size()-1);
            return latestMetric.getValue();
        }
        return 0.0;
    }
}
