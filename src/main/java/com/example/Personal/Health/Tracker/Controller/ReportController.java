package com.example.Personal.Health.Tracker.Controller;

import com.example.Personal.Health.Tracker.Service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @GetMapping("/user/{userId}/weekly")
    public ResponseEntity<String> generateWeeklyReport(@PathVariable Long userId){
        String report = reportService.generateWeeklyReport(userId);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/monthly")
    public ResponseEntity<String> generateMonthlyReport(@PathVariable Long userId){
        String report = reportService.generateMonthlyReport(userId);
        return new ResponseEntity<>(report, HttpStatus.OK);
    }
}
