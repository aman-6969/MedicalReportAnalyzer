package com.medical.analyzer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class ReportAnalysisService {

    public String analyzeReport(MultipartFile file) throws IOException {
        // Step 1: Read the text content directly for testing
        String extractedText = new String(file.getBytes(), StandardCharsets.UTF_8);
        log.info("Text extracted from document: {}", extractedText);
        
        // For testing, create a simple analysis
        StringBuilder analysis = new StringBuilder();
        analysis.append("=== Medical Report Analysis ===\n\n");
        analysis.append("1. Report Overview:\n");
        analysis.append("   - Successfully processed the medical report\n");
        analysis.append("   - File size: ").append(file.getSize()).append(" bytes\n\n");
        analysis.append("2. Report Content:\n");
        analysis.append(extractedText);
        
        return analysis.toString();
    }
}