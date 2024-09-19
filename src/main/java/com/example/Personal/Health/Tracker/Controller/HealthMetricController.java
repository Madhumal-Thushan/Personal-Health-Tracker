package com.example.Personal.Health.Tracker.Controller;

import com.example.Personal.Health.Tracker.Dto.HealthMetricDto;
import com.example.Personal.Health.Tracker.Entity.HealthMetric;
import com.example.Personal.Health.Tracker.Service.HealthMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Optional;

@RestController
@RequestMapping("/api/metrics")
public class HealthMetricController {

    @Autowired
    private HealthMetricService healthMetricService;


    @PostMapping("/add")
    public ResponseEntity<HealthMetric> addHealthMetrics(@RequestBody HealthMetric healthMetric) {
        HealthMetric createdMetric = healthMetricService.addHealthMetrics(healthMetric);
        return new ResponseEntity<>(createdMetric, HttpStatus.CREATED);
    }

    @PutMapping("/edit")
    public ResponseEntity<HealthMetric> updateHealthMetric(
            @RequestParam Long id,
            @RequestBody HealthMetricDto updatedMetric) {
       HealthMetric healthMetric =  healthMetricService.updateHealthMetric(id, updatedMetric);
        return new ResponseEntity<>(healthMetric,HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<Optional<HealthMetric>> getHealthMetric(@RequestParam Long id) {
        Optional<HealthMetric> healthMetric = healthMetricService.getHealthMetricById(id);
        return new ResponseEntity<>(healthMetric,HttpStatus.OK);
    }
}
