package com.jobportal.service.ai;

import com.jobportal.dto.ai.AsiOneRequest;
import com.jobportal.dto.ai.AsiOneResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * Core service for interacting with the ASI:ONE LLM API.
 * Handles authentication, request building, error handling, and retries.
 */
@Service
@Slf4j
public class AsiOneService {

    private final RestTemplate restTemplate;
    private final String apiKey;
    private final String baseUrl;
    private final String model;

    private static final int MAX_RETRIES = 3;
    private static final long RETRY_DELAY_MS = 1000;

    public AsiOneService(
            @Qualifier("asiOneRestTemplate") RestTemplate restTemplate,
            @Qualifier("asiOneApiKey") String apiKey,
            @Qualifier("asiOneBaseUrl") String baseUrl,
            @Qualifier("asiOneModel") String model) {
        this.restTemplate = restTemplate;
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
        this.model = model;
    }

    /**
     * Check if the API is properly configured.
     */
    public boolean isAvailable() {
        return apiKey != null && !apiKey.isBlank() && !"your_api_key_here".equals(apiKey);
    }

    /**
     * Send a chat completion request with system prompt and user message.
     */
    public String chatCompletion(String systemPrompt, String userMessage) {
        return chatCompletion(systemPrompt, userMessage, null);
    }

    /**
     * Send a chat completion request with optional session ID for context continuity.
     */
    public String chatCompletion(String systemPrompt, String userMessage, String sessionId) {
        if (!isAvailable()) {
            log.warn("ASI:ONE API not configured. Returning fallback response.");
            return null;
        }

        AsiOneRequest request = AsiOneRequest.builder()
                .model(model)
                .messages(List.of(
                        AsiOneRequest.Message.builder().role("system").content(systemPrompt).build(),
                        AsiOneRequest.Message.builder().role("user").content(userMessage).build()
                ))
                .stream(false)
                .build();

        return executeWithRetry(request, sessionId);
    }

    /**
     * Send a chat completion with full message history (for chatbot context).
     */
    public String chatCompletionWithHistory(List<AsiOneRequest.Message> messages, String sessionId) {
        if (!isAvailable()) {
            log.warn("ASI:ONE API not configured. Returning fallback response.");
            return null;
        }

        AsiOneRequest request = AsiOneRequest.builder()
                .model(model)
                .messages(messages)
                .stream(false)
                .build();

        return executeWithRetry(request, sessionId);
    }

    /**
     * Execute the API call with retry logic.
     */
    private String executeWithRetry(AsiOneRequest request, String sessionId) {
        String url = baseUrl + "/chat/completions";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);
        if (sessionId != null && !sessionId.isBlank()) {
            headers.set("x-session-id", sessionId);
        }

        HttpEntity<AsiOneRequest> entity = new HttpEntity<>(request, headers);

        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                log.debug("ASI:ONE API call attempt {}/{}", attempt, MAX_RETRIES);
                ResponseEntity<AsiOneResponse> response = restTemplate.exchange(
                        url, HttpMethod.POST, entity, AsiOneResponse.class);

                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    String reply = response.getBody().getReply();
                    if (reply != null) {
                        log.debug("ASI:ONE API response received ({} tokens)",
                                response.getBody().getUsage() != null ?
                                        response.getBody().getUsage().getTotal_tokens() : "unknown");
                        return reply;
                    }
                }
                log.warn("ASI:ONE API returned empty response on attempt {}", attempt);
            } catch (RestClientException e) {
                log.error("ASI:ONE API call failed on attempt {}/{}: {}", attempt, MAX_RETRIES, e.getMessage());
                if (attempt < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS * attempt); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        }

        log.error("ASI:ONE API call failed after {} retries", MAX_RETRIES);
        return null;
    }
}
