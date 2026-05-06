package com.jobportal.controller.api;

import com.jobportal.dto.ai.ChatRequest;
import com.jobportal.dto.ai.ChatResponse;
import com.jobportal.dto.shared.ApiResponse;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.ai.ChatbotService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatbotApiController {

    private final ChatbotService chatbotService;

    @PostMapping
    public ResponseEntity<ApiResponse<ChatResponse>> chat(@RequestBody ChatRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        String sessionId = request.getSessionId() != null ? request.getSessionId() : UUID.randomUUID().toString();
        ChatResponse response = chatbotService.chat(userId, request.getMessage(), sessionId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @DeleteMapping("/session")
    public ResponseEntity<ApiResponse<String>> clearSession(@RequestParam String sessionId) {
        chatbotService.clearSession(sessionId);
        return ResponseEntity.ok(ApiResponse.success("Session cleared"));
    }
}
