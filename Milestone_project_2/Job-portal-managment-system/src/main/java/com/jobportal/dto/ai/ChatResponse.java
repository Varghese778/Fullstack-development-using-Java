package com.jobportal.dto.ai;

import lombok.*;
import java.util.List;

/**
 * Chat response returned to the frontend chatbot widget.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse {

    /**
     * The assistant's reply text.
     */
    private String reply;

    /**
     * Session ID for maintaining context in follow-up messages.
     */
    private String sessionId;

    /**
     * Optional: suggested follow-up questions/actions.
     */
    private List<String> suggestions;

    /**
     * Whether the response was generated successfully.
     */
    @Builder.Default
    private boolean success = true;

    /**
     * Error message if generation failed.
     */
    private String error;
}
