package com.example.Personal.Health.Tracker.Controller;

import com.example.Personal.Health.Tracker.Dto.HealthGoalDto;
import com.example.Personal.Health.Tracker.Dto.HealthGoalUpdateDto;
import com.example.Personal.Health.Tracker.Dto.Response.GoalResponse;
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
    public ResponseEntity<GoalResponse> createGoal(@RequestBody HealthGoalDto goal) {
        return new ResponseEntity<>(healthGoalService.setHealthGoal(goal), HttpStatus.CREATED);
    }

    @GetMapping("/get")
    public ResponseEntity<List<GoalResponse>> getHealthGoalByUser(@RequestParam Long userId){
        return new ResponseEntity<>(healthGoalService.getHealthGoalByUser(userId), HttpStatus.OK);
    }

    @PutMapping("/edit")
    public ResponseEntity<GoalResponse> updateHealthGoal(@RequestParam Long id, @RequestBody HealthGoalUpdateDto updateDto) {
        return  ResponseEntity.ok(healthGoalService.updateHealthGoal(id,updateDto));
    }

    @PostMapping("/users/track-progress")
    public ResponseEntity<List<GoalResponse>> trackProgress(@RequestParam Long userId) {
        return new ResponseEntity<>(healthGoalService.trackGoalProgress(userId),HttpStatus.OK);
    }
}
