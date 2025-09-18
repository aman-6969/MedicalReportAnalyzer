package com.medical.analyzer.service;

import com.medical.analyzer.service.integration.OCRService;
import com.medical.analyzer.service.integration.ChatbotService;
import com.medical.analyzer.service.integration.HuggingFaceService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;

@Service
@Slf4j
public class ReportAnalysisService {

    private final OCRService ocrService;
    private final ChatbotService chatbotService;
    private final HuggingFaceService huggingFaceService;

    public ReportAnalysisService(OCRService ocrService,
                                ChatbotService chatbotService,
                                HuggingFaceService huggingFaceService) {
        this.ocrService = ocrService;
        this.chatbotService = chatbotService;
        this.huggingFaceService = huggingFaceService;
    }

    public String analyzeReport(MultipartFile file) throws IOException {
        // Step 1: Extract text using friend's OCR service (with fallback)
        String extractedText = ocrService.extractText(file);
        log.info("Text extracted from document");

        // Step 2: Get initial analysis from friend's chatbot
        String chatbotAnalysis = chatbotService.analyzeReport(extractedText);
        log.info("Initial analysis completed by chatbot");

        // Step 3: Enhanced medical analysis using Hugging Face
        String medicalAnalysis = huggingFaceService.analyzeMedicalText(extractedText);
        log.info("Medical analysis completed by Hugging Face");

        // Step 4: Combine both analyses
        return combineAnalyses(chatbotAnalysis, medicalAnalysis);
    }

    private String combineAnalyses(String chatbotAnalysis, String medicalAnalysis) {
        return """
            === Chatbot Assistant Analysis ===
            %s

            === Medical Model Analysis ===
            %s
            """.formatted(chatbotAnalysis, medicalAnalysis);
    }
}