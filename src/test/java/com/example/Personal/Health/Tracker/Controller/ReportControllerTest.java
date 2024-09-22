package com.example.Personal.Health.Tracker.Controller;

import com.example.Personal.Health.Tracker.Dto.MonthlyReportDto;
import com.example.Personal.Health.Tracker.Dto.WeeklyReportDto;
import com.example.Personal.Health.Tracker.Service.ReportService;
import com.example.Personal.Health.Tracker.Utils.PDFReportGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ReportControllerTest {
    @Mock
    private ReportService reportService;

    @Mock
    private PDFReportGenerator pdfReportGenerator;

    @InjectMocks
    private ReportController reportController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateWeeklyReport_ShouldReturnWeeklyReport() {
        Long userId = 1L;
        WeeklyReportDto weeklyReportDto = new WeeklyReportDto(250.2,100.5,100.5);

        when(reportService.generateWeeklyReport(userId)).thenReturn(weeklyReportDto);

        ResponseEntity<WeeklyReportDto> responseEntity = reportController.generateWeeklyReport(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(weeklyReportDto, responseEntity.getBody());

        verify(reportService, times(1)).generateWeeklyReport(userId);
    }

    @Test
    void generateMonthlyReport_ShouldReturnMonthlyReport() {
        Long userId = 1L;
        MonthlyReportDto monthlyReportDto = new MonthlyReportDto(250.2,100.5,100.5);

        when(reportService.generateMonthlyReport(userId)).thenReturn(monthlyReportDto);

        ResponseEntity<MonthlyReportDto> responseEntity = reportController.generateMonthlyReport(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(monthlyReportDto, responseEntity.getBody());

        verify(reportService, times(1)).generateMonthlyReport(userId);
    }

    @Test
    void generateWeeklyPDFReport_ShouldReturnPDF() {
        Long userId = 1L;
        WeeklyReportDto weeklyReportDto = new WeeklyReportDto(250.2,100.5,100.5);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        when(reportService.generateWeeklyReport(userId)).thenReturn(weeklyReportDto);
        when(pdfReportGenerator.generateWeeklyPDFReport(weeklyReportDto)).thenReturn(byteArrayInputStream);

        ResponseEntity<InputStreamResource> responseEntity = reportController.generateWeeklyPDFReport(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("application/pdf", responseEntity.getHeaders().getContentType().toString());
        assertEquals("Inline;filename=monthly_report.pdf", responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));

        verify(reportService, times(1)).generateWeeklyReport(userId);
        verify(pdfReportGenerator, times(1)).generateWeeklyPDFReport(weeklyReportDto);
    }

    @Test
    void generateMonthlyPDFReport_ShouldReturnPDF() {
        Long userId = 1L;
        MonthlyReportDto monthlyReportDto = new MonthlyReportDto(250.2,100.5,100.5);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(new byte[0]);
        when(reportService.generateMonthlyReport(userId)).thenReturn(monthlyReportDto);
        when(pdfReportGenerator.generateMonthlyPDFReport(monthlyReportDto)).thenReturn(byteArrayInputStream);

        ResponseEntity<InputStreamResource> responseEntity = reportController.generateMonthlyPDFReport(userId);

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("application/pdf", responseEntity.getHeaders().getContentType().toString());
        assertEquals("Inline;filename=weekly_report_report.pdf", responseEntity.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));

        verify(reportService, times(1)).generateMonthlyReport(userId);
        verify(pdfReportGenerator, times(1)).generateMonthlyPDFReport(monthlyReportDto);
    }
}
