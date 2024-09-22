package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Dto.HealthGoalDto;
import com.example.Personal.Health.Tracker.Dto.HealthGoalUpdateDto;
import com.example.Personal.Health.Tracker.Dto.Response.GoalResponse;
import com.example.Personal.Health.Tracker.Dto.Response.HealthMetricResponse;
import com.example.Personal.Health.Tracker.Dto.UserDto;
import com.example.Personal.Health.Tracker.Entity.Goal;
import com.example.Personal.Health.Tracker.Entity.Users;
import com.example.Personal.Health.Tracker.Enum.GoalType;
import com.example.Personal.Health.Tracker.Exception.ResourceNotFoundException;
import com.example.Personal.Health.Tracker.Repository.HealthGoalRepository;
import com.example.Personal.Health.Tracker.Repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;


    /**
     * Add health Goal
     * @param dto
     * @return
     */
    public GoalResponse setHealthGoal(HealthGoalDto dto) {
        Users user = userRepository.findById(dto.getUserId()).orElseThrow(() -> new ResourceNotFoundException("User not found" + dto.getUserId()));
        Goal goal = new Goal();
        goal.setUsers(user);
        goal.setGoalType(dto.getGoalType());
        goal.setTargetValue(dto.getTargetValue());
        goal.setCreatedAt(LocalDate.now());
        goal.setStartDate(dto.getStartDate());
        goal.setEndDate(dto.getEndDate());
        Goal savedGoal =healthGoalRepository.save(goal);
        return convertToResponseDTO(savedGoal);
    }

    /**
     * Get Health Goal bu User
     * @param userId
     * @return
     */
    public List<GoalResponse> getHealthGoalByUser(Long userId) {
        List<Goal> goalList = healthGoalRepository.findByUsersId(userId);
        if(goalList.isEmpty()) {
            throw new ResourceNotFoundException("Health Goal not found for user ID : " +userId);
        }
        return goalList.stream()
                .map(this::convertToResponseDTO)
                .toList();
    }

    /**
     * This Private method Used to Convert according to Goal Response
     * @param goal
     * @return
     */
    private GoalResponse convertToResponseDTO(Goal goal) {
        GoalResponse responseDTO = new GoalResponse();
        responseDTO.setId(goal.getId());
        responseDTO.setIsActive(goal.getIsActive());
        responseDTO.setGoalType(goal.getGoalType());
        responseDTO.setTargetValue(goal.getTargetValue());
        responseDTO.setAchieved(goal.getAchieved());
        responseDTO.setStartDate(goal.getStartDate());
        responseDTO.setEndDate(goal.getEndDate());
        responseDTO.setCreatedAt(goal.getCreatedAt());
        responseDTO.setUpdatedAt(goal.getUpdatedAt());

        UserDto userDTO = new UserDto();
        userDTO.setId(goal.getUsers().getId());
        userDTO.setUsername(goal.getUsers().getUsername());
        responseDTO.setUser(userDTO);

        return responseDTO;
    }

    /**
     * This method use to Update Existing Goal
     * @param id
     * @param updateDto
     * @return
     */
    @Transactional
    public GoalResponse updateHealthGoal(Long id, HealthGoalUpdateDto updateDto) {
        Goal existingGoal = healthGoalRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        existingGoal.setTargetValue(updateDto.getTargetValue());
        existingGoal.setIsActive(updateDto.getIsActive());
        existingGoal.setEndDate(updateDto.getStartDate());
        existingGoal.setUpdatedAt(LocalDate.now());

        if (updateDto.getUserId() != null) {
            Users user = userRepository.findById(updateDto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));
            existingGoal.setUsers(user);
        }
        Goal updatedGoal = healthGoalRepository.save(existingGoal);
        return convertToResponseDTO(updatedGoal);
    }

    /**
     * Track Goal Progress based on Health Metrics
     * @param id
     * @return
     */
    public List<GoalResponse> trackGoalProgress(Long id) {
        List<Goal> goals = healthGoalRepository.findByUsersId(id);
        if (goals == null || goals.isEmpty()) {
            throw new ResourceNotFoundException("No goals found for user: " + id);
        }
        List<HealthMetricResponse> healthMetricList = healthMetricService.getHealthMetricsByUser(id);
        List<GoalResponse> goalResponseList = new ArrayList<>();
        goals.forEach( goal -> {
            healthMetricList.forEach(healthMetric -> {
                // Check if the goal is not achieved and is still active
                if (!goal.getAchieved() && !goal.getIsActive()) {
                    if (goal.getGoalType() == GoalType.Weight_Goal && healthMetric.getValue() < goal.getTargetValue()) {
                        goal.setAchieved(true);
                    } else if (goal.getGoalType() == GoalType.Step_Goal && healthMetric.getValue() > goal.getTargetValue()) {
                        goal.setAchieved(true);
                    } else if (goal.getGoalType() == GoalType.Calories_Goal && healthMetric.getValue() < goal.getTargetValue()) {
                        goal.setAchieved(true);
                    }
                }
            });
            healthGoalRepository.save(goal);
            GoalResponse response = new GoalResponse();
            response.setId(goal.getId());
            response.setGoalType(goal.getGoalType());
            response.setTargetValue(goal.getTargetValue());
            response.setStartDate(goal.getStartDate());
            response.setEndDate(goal.getEndDate());
            response.setAchieved(goal.getAchieved());
            goalResponseList.add(response);
        });
        return goalResponseList;
    }
}
