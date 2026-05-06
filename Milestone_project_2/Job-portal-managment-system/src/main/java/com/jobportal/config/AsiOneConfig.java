package com.jobportal.config;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * Configuration for ASI:ONE LLM API integration.
 * Reads API key from .env file and configures a dedicated RestTemplate.
 */
@Configuration
@Slf4j
public class AsiOneConfig {

    @Value("${asi.one.api-key:}")
    private String apiKeyFromProperties;

    @Value("${asi.one.base-url:https://api.asi1.ai/v1}")
    private String baseUrl;

    @Value("${asi.one.model:asi1}")
    private String model;

    @Value("${asi.one.timeout:30000}")
    private int timeout;

    @Bean
    public Dotenv dotenv() {
        return Dotenv.configure()
                .directory(".")
                .ignoreIfMissing()
                .load();
    }

    /**
     * Returns the API key, preferring .env file over application.properties.
     */
    @Bean
    public String asiOneApiKey(Dotenv dotenv) {
        String key = dotenv.get("ASI_ONE_API_KEY");
        if (key == null || key.isBlank() || "your_api_key_here".equals(key)) {
            key = apiKeyFromProperties;
        }
        if (key == null || key.isBlank()) {
            log.warn("ASI:ONE API key not configured. AI features will be unavailable.");
        } else {
            log.info("ASI:ONE API key configured successfully.");
        }
        return key != null ? key : "";
    }

    @Bean
    public String asiOneBaseUrl() {
        return baseUrl;
    }

    @Bean
    public String asiOneModel() {
        return model;
    }

    /**
     * Dedicated RestTemplate for ASI:ONE API calls with appropriate timeouts.
     */
    @Bean("asiOneRestTemplate")
    public RestTemplate asiOneRestTemplate() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(timeout);
        factory.setReadTimeout(timeout);
        return new RestTemplate(factory);
    }
}
