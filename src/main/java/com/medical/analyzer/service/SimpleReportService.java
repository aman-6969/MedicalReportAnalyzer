package com.medical.analyzer.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Service
public class SimpleReportService {
    
    public String analyzeReport(MultipartFile file) throws IOException {
        String content = new String(file.getBytes(), StandardCharsets.UTF_8);
        return generateAnalysis(content, file.getOriginalFilename());
    }
    
    private String generateAnalysis(String content, String filename) {
        StringBuilder analysis = new StringBuilder();
        analysis.append("=== Medical Report Analysis ===\n\n");
        
        // Split content into sections
        String[] lines = content.split("\n");
        StringBuilder currentSection = new StringBuilder();
        String currentHeader = "";
        
        analysis.append("Summary:\n");
        
        // Process the content by sections
        for (String line : lines) {
            line = line.trim();
            
            // Skip empty lines
            if (line.isEmpty()) {
                continue;
            }
            
            // Check if this is a header line (all caps or ends with :)
            if (line.equals(line.toUpperCase()) || line.endsWith(":")) {
                // If we have a previous section, analyze it
                if (currentSection.length() > 0) {
                    analysis.append("\n").append(currentHeader).append(":\n");
                    analysis.append("- ").append(summarizeSection(currentSection.toString())).append("\n");
                }
                
                // Start new section
                currentHeader = line.replace(":", "").trim();
                currentSection = new StringBuilder();
            } else {
                // Add line to current section
                currentSection.append(line).append(" ");
            }
        }
        
        // Process the last section
        if (currentSection.length() > 0) {
            analysis.append("\n").append(currentHeader).append(":\n");
            analysis.append("- ").append(summarizeSection(currentSection.toString())).append("\n");
        }
        
        return analysis.toString();
    }
    
    private String summarizeSection(String section) {
        // Remove extra whitespace
        section = section.replaceAll("\\s+", " ").trim();
        
        // If section is short enough, return as is
        if (section.length() < 100) {
            return section;
        }
        
        // Get the first sentence or first 100 characters
        int endOfSentence = section.indexOf(". ");
        if (endOfSentence > 0 && endOfSentence < 150) {
            return section.substring(0, endOfSentence + 1);
        } else {
            // Get first 100 chars and cut at last space to avoid word breaks
            int cutoff = Math.min(150, section.length());
            int lastSpace = section.lastIndexOf(" ", cutoff);
            return section.substring(0, lastSpace) + "...";
        }
    }
}