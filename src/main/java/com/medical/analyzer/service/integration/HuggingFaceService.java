package com.medical.analyzer.service.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class HuggingFaceService {
    private final WebClient webClient;
    private final String apiKey;
    private final String modelId;

    public HuggingFaceService(WebClient.Builder webClientBuilder,
                             @Value("${huggingface.api.url}") String apiUrl,
                             @Value("${huggingface.api.key}") String apiKey,
                             @Value("${huggingface.model.medical}") String modelId) {
        this.webClient = webClientBuilder
                .baseUrl(apiUrl)
                .defaultHeader("Authorization", "Bearer " + apiKey)
                .build();
        this.apiKey = apiKey;
        this.modelId = modelId;
    }

    public String analyzeMedicalText(String text) {
        try {
            return webClient.post()
                    .uri("/models/" + modelId)
                    .bodyValue(text)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Error calling Hugging Face service", e);
            return "Error analyzing with Hugging Face: " + e.getMessage();
        }
    }
}