package com.medical.analyzer.service.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OCRService {
    private final WebClient webClient;
    private final String ocrServiceUrl;

    public OCRService(WebClient.Builder webClientBuilder, 
                     @Value("${friend.ocr.service.url}") String ocrServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(ocrServiceUrl).build();
        this.ocrServiceUrl = ocrServiceUrl;
    }

    public String extractText(MultipartFile file) {
        try {
            // Call friend's OCR service
            return webClient.post()
                    .uri("/extract")
                    .bodyValue(file.getBytes())
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Error calling OCR service", e);
            // Fallback to PDFBox for PDF files
            return extractTextLocally(file);
        }
    }

    private String extractTextLocally(MultipartFile file) {
        // Implementation using PDFBox as fallback
        // ... (existing PDFBox code)
        return "Fallback text extraction";
    }
}