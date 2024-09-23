package com.example.Personal.Health.Tracker.Service;

import com.example.Personal.Health.Tracker.Dto.MonthlyReportDto;
import com.example.Personal.Health.Tracker.Dto.WeeklyReportDto;
import com.example.Personal.Health.Tracker.Exception.ResourceNotFoundException;
import com.example.Personal.Health.Tracker.Repository.HealthMetricRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

class ReportServiceTest {

    @InjectMocks
    private ReportService reportService;

    @Mock
    private HealthMetricRepository healthMetricRepository;

    @Mock
    private HealthMetricService healthMetricService;

    @Mock
    private HealthGoalService goalService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGenerateWeeklyReport_Success() {
        Long userId = 1L;
        WeeklyReportDto expectedReport = new WeeklyReportDto(150.0,200.0,250.0);

        when(healthMetricRepository.getWeeklyAggregatedData(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(expectedReport);

        WeeklyReportDto result = reportService.generateWeeklyReport(userId);

        assertEquals(expectedReport, result);
    }

    @Test
    void testGenerateWeeklyReport_NoReportsFound() {
        Long userId = 1L;

        when(healthMetricRepository.getWeeklyAggregatedData(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(null);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            reportService.generateWeeklyReport(userId);
        });

        assertEquals("No Reports Found for user : " + userId, thrown.getMessage());
    }

    @Test
    void testGenerateMonthlyReport_Success() {
        Long userId = 1L;
        MonthlyReportDto expectedReportDto = new MonthlyReportDto(150.0,200.0,250.0);
        // Set properties on expectedReportDto as necessary

        when(healthMetricRepository.getMonthlyAggregatedData(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(expectedReportDto);

        MonthlyReportDto result = reportService.generateMonthlyReport(userId);

        assertEquals(expectedReportDto, result);
    }

    @Test
    void testGenerateMonthlyReport_NoReportsFound() {
        Long userId = 1L;

        when(healthMetricRepository.getMonthlyAggregatedData(anyLong(), any(LocalDate.class), any(LocalDate.class)))
                .thenReturn(null);

        ResourceNotFoundException thrown = assertThrows(ResourceNotFoundException.class, () -> {
            reportService.generateMonthlyReport(userId);
        });

        assertEquals("No Reports Found for user : " + userId, thrown.getMessage());
    }
}