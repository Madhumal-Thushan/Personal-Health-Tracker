package com.example.Personal.Health.Tracker.Utils;

import com.example.Personal.Health.Tracker.Dto.MonthlyReportDto;
import com.example.Personal.Health.Tracker.Dto.WeeklyReportDto;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

@Service
public class PDFReportGenerator {

    public ByteArrayInputStream generateWeeklyPDFReport(WeeklyReportDto weeklyReportDto) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            PdfWriter.getInstance(document,outputStream);
            document.open();
            document.add(new Paragraph("Weekly Health Report"));
            document.add(new Paragraph(" Weight Change" + weeklyReportDto.getWeightChange()));
            document.add(new Paragraph("Average Steps" +weeklyReportDto.getAverageSteps()));
            document.add(new Paragraph("Average Calories" +weeklyReportDto.getAverageCalories()));
            document.close();

        } catch (DocumentException exception) {
            exception.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }

    public ByteArrayInputStream generateMonthlyPDFReport(MonthlyReportDto monthlyReportDto) {
        Document document = new Document();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document,outputStream);
            document.open();
            document.add(new Paragraph("Monthly Health Report"));
            document.add(new Paragraph(" Weight Change" + monthlyReportDto.getWeightChange()));
            document.add(new Paragraph("Average Steps" +monthlyReportDto.getAverageSteps()));
            document.add(new Paragraph("Average Calories" +monthlyReportDto.getAverageCalories()));
            document.close();

        } catch (DocumentException exception) {
            exception.printStackTrace();
        }
        return new ByteArrayInputStream(outputStream.toByteArray());
    }
}
