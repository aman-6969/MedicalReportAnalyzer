package com.medical.analyzer.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.medical.analyzer.service.ReportAnalysisService;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportAnalysisService reportAnalysisService;

    public ReportController(ReportAnalysisService reportAnalysisService) {
        this.reportAnalysisService = reportAnalysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<String> analyzeReport(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("Please upload a file");
        }

        String contentType = file.getContentType();
        if (contentType == null || (!contentType.equals("application/pdf") && !contentType.equals("text/plain"))) {
            return ResponseEntity.badRequest().body("Only PDF and text files are supported");
        }

        try {
            String analysis = reportAnalysisService.analyzeReport(file);
            return ResponseEntity.ok(analysis);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error analyzing report: " + e.getMessage());
        }
    }
}