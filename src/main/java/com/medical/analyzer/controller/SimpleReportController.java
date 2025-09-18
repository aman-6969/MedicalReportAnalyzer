package com.medical.analyzer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.medical.analyzer.service.SimpleReportService;

@RestController
@RequestMapping("/api/report")
public class SimpleReportController {

    private final SimpleReportService reportService;

    public SimpleReportController(SimpleReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeReport(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please upload a file");
            }
            String analysis = reportService.analyzeReport(file);
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error processing file: " + e.getMessage());
        }
    }
}