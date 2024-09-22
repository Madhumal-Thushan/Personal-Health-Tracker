package com.example.Personal.Health.Tracker.Controller;

import com.example.Personal.Health.Tracker.Dto.MonthlyReportDto;
import com.example.Personal.Health.Tracker.Dto.WeeklyReportDto;
import com.example.Personal.Health.Tracker.Service.ReportService;
import com.example.Personal.Health.Tracker.Utils.PDFReportGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayInputStream;

@RestController
@RequestMapping("/api/report")
public class ReportController {
    @Autowired
    private ReportService reportService;
    @Autowired
    private PDFReportGenerator pdfReportGenerator;


    /**
     * This Method to Get JSON Response For Weekly Reports
     * @param userId
     * @return
     */
    @GetMapping("/user/weekly")
    public ResponseEntity<WeeklyReportDto> generateWeeklyReport(@RequestParam Long userId){
        return new ResponseEntity<>(reportService.generateWeeklyReport(userId), HttpStatus.OK);
    }

    /**
     * This method to get JSON response
     * @param userId
     * @return
     */
    @GetMapping("/user/monthly")
    public ResponseEntity<MonthlyReportDto> generateMonthlyReport(@RequestParam Long userId){
        return new ResponseEntity<>(reportService.generateMonthlyReport(userId), HttpStatus.OK);
    }

    /**
     * Developed This method Additionally to Get PDF Reports
     * @param userId
     * @return
     */
    @GetMapping("/user/weekly/pdf")
    public ResponseEntity<InputStreamResource> generateWeeklyPDFReport(@RequestParam Long userId){
        WeeklyReportDto weeklyReportDto = reportService.generateWeeklyReport(userId);
        ByteArrayInputStream byteArrayInputStream = pdfReportGenerator.generateWeeklyPDFReport(weeklyReportDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition"," Inline;filename=monthly_report.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(byteArrayInputStream));
    }
    @GetMapping("/user/monthly/pdf")
    public ResponseEntity<InputStreamResource> generateMonthlyPDFReport(@RequestParam Long userId){
        MonthlyReportDto monthlyReportDto = reportService.generateMonthlyReport(userId);
        ByteArrayInputStream byteArrayInputStream = pdfReportGenerator.generateMonthlyPDFReport(monthlyReportDto);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition"," Inline;filename=weekly_report_report.pdf");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(new InputStreamResource(byteArrayInputStream));
    }
}
