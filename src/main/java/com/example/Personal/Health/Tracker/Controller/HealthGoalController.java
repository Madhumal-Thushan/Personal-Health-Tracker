package com.example.Personal.Health.Tracker.Controller;

import com.example.Personal.Health.Tracker.Dto.HealthGoalDto;
import com.example.Personal.Health.Tracker.Entity.Goal;
import com.example.Personal.Health.Tracker.Service.HealthGoalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
