package com.example.Personal.Health.Tracker.Controller;

import com.example.Personal.Health.Tracker.Dto.HealthGoalDto;
import com.example.Personal.Health.Tracker.Entity.Goal;
import com.example.Personal.Health.Tracker.Service.HealthGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/healthGoal")
public class HealthGoalController {

    @Autowired
    private HealthGoalService healthGoalService;
    @PostMapping("/add")
    public ResponseEntity<HealthGoalDto> createGoal(@RequestBody Goal goal) {
        HealthGoalDto createdGoal = healthGoalService.setHealthGoal(goal);
        return new ResponseEntity<>(createdGoal, HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<List<HealthGoalDto>> getHealthGoalByUser(@RequestParam Long userId){
        List<HealthGoalDto> goalDtos = healthGoalService.getHealthGoalByUser(userId);
        return new ResponseEntity<>(goalDtos, HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<HealthGoalDto> updateHealthGoal(@RequestParam Long id, @RequestBody Goal goal) {
        return  ResponseEntity.ok(healthGoalService.updateHealthGoal(id,goal));
    }

    @PostMapping("/users/track-progress")
    public ResponseEntity<List<HealthGoalDto>> trackProgress(@RequestParam Long userId) {
        healthGoalService.trackGoalProgress(userId);
        List<HealthGoalDto> updatedGoals = healthGoalService.getHealthGoalByUser(userId);
        return new ResponseEntity<>(updatedGoals,HttpStatus.OK);
    }
}
