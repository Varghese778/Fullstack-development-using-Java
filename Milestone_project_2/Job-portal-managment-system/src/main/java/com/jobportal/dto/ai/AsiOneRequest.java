package com.jobportal.dto.ai;

import lombok.*;
import java.util.List;

/**
 * Request DTO matching the ASI:ONE (OpenAI-compatible) chat completion API.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsiOneRequest {

    private String model;
    private List<Message> messages;

    @Builder.Default
    private Boolean stream = false;

    @Builder.Default
    private Boolean web_search = false;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;    // "system", "user", "assistant"
        private String content;
    }
}
