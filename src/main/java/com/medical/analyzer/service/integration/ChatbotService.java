package com.medical.analyzer.service.integration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
@Slf4j
public class ChatbotService {
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    public ChatbotService(WebClient.Builder webClientBuilder,
                         @Value("${friend.chatbot.service.url}") String chatbotServiceUrl) {
        this.webClient = webClientBuilder.baseUrl(chatbotServiceUrl).build();
        this.objectMapper = new ObjectMapper();
    }

    public String analyzeReport(String text) {
        try {
            return webClient.post()
                    .uri("/analyze")
                    .bodyValue(text)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            log.error("Error calling Chatbot service", e);
            return "Error analyzing report: " + e.getMessage();
        }
    }
}