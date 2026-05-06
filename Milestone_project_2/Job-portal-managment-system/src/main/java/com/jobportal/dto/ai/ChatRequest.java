package com.jobportal.dto.ai;

import lombok.*;

/**
 * Chat request from the frontend chatbot widget.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {

    /**
     * The user's message text.
     */
    private String message;

    /**
     * Session ID for maintaining conversation context.
     */
    private String sessionId;
}
