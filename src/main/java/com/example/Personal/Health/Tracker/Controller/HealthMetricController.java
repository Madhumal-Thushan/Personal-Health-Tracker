package com.example.Personal.Health.Tracker.Controller;

import com.example.Personal.Health.Tracker.Dto.HealthMetricDto;
import com.example.Personal.Health.Tracker.Dto.Response.HealthMetricResponse;
import com.example.Personal.Health.Tracker.Service.HealthMetricService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/metrics")
public class HealthMetricController {

    @Autowired
    private HealthMetricService healthMetricService;


    @PostMapping("/add")
    public ResponseEntity<HealthMetricResponse> addHealthMetrics(@RequestBody HealthMetricDto requestDto) {
        return new ResponseEntity<>(healthMetricService.addHealthMetrics(requestDto), HttpStatus.CREATED);
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<HealthMetricResponse>> getHealthMetricsByUser(@RequestParam Long userId){
        return new ResponseEntity<>(healthMetricService.getHealthMetricsByUser(userId),HttpStatus.OK);
    }

    @GetMapping("/getHealthMetric")
    public ResponseEntity<HealthMetricResponse> getHealthMetricById(@RequestParam Long id) {
        return ResponseEntity.ok(healthMetricService.getHealthMetricById(id));
    }

    @PutMapping("/edit")
    public ResponseEntity<HealthMetricResponse> updateHealthMetric(@RequestParam Long id, @RequestBody HealthMetricDto metric) {
        return ResponseEntity.ok(healthMetricService.updateHealthMetric(id, metric));
    }
}
