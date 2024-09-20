package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Dto.HealthGoalDto;
import com.example.Personal.Health.Tracker.Entity.Goal;
import com.example.Personal.Health.Tracker.Repository.HealthGoalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HealthGoalService {

    @Autowired
    private HealthGoalRepository healthGoalRepository;

    @Autowired
    private HealthMetricService healthMetricService;


    public HealthGoalDto setHealthGoal(Goal goal){
        HealthGoalDto dto = new HealthGoalDto();
        dto.setGoalType(goal.getGoalType());
        dto.setId(goal.getId());
        dto.setTargetValue(goal.getTargetValue());
        dto.setAchieved(goal.getAchieved());
        healthGoalRepository.save(goal);
        return dto;
    }
}
