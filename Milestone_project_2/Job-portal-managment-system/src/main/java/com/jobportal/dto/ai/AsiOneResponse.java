package com.jobportal.dto.ai;

import lombok.*;
import java.util.List;

/**
 * Response DTO matching the ASI:ONE (OpenAI-compatible) chat completion API.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AsiOneResponse {

    private String id;
    private List<Choice> choices;
    private Usage usage;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {
        private int index;
        private Message message;
        private String finish_reason;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Message {
        private String role;
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Usage {
        private int prompt_tokens;
        private int completion_tokens;
        private int total_tokens;
    }

    /**
     * Convenience: extract the assistant's reply text.
     */
    public String getReply() {
        if (choices != null && !choices.isEmpty() && choices.get(0).getMessage() != null) {
            return choices.get(0).getMessage().getContent();
        }
        return null;
    }
}
