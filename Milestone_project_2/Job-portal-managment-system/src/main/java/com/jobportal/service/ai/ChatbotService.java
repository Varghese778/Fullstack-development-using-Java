package com.jobportal.service.ai;

import com.jobportal.dto.ai.AsiOneRequest;
import com.jobportal.dto.ai.ChatResponse;
import com.jobportal.dto.ai.ResumeProfile;
import com.jobportal.dto.ai.WeightedSkill;
import com.jobportal.entity.Job;
import com.jobportal.enums.JobStatusEnum;
import com.jobportal.repository.JobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * Context-aware chatbot service powered by ASI:ONE.
 * Maintains session context with resume + job listing awareness.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ChatbotService {

    private final AsiOneService asiOneService;
    private final JobRecommendationService recommendationService;
    private final JobRepository jobRepository;

    // In-memory session storage (last 20 messages per session)
    private final Map<String, List<AsiOneRequest.Message>> sessionHistory = new ConcurrentHashMap<>();
    private static final int MAX_HISTORY_SIZE = 20;

    private static final String SYSTEM_PROMPT = """
            You are JobBot, an AI career assistant for the Job Portal platform. You help job seekers with:
            - Finding suitable jobs based on their skills and experience
            - Explaining job recommendations and why certain roles match
            - Answering questions about specific job postings
            - Providing career advice and interview tips
            - Suggesting skills to develop for desired roles
            
            Guidelines:
            - Be friendly, encouraging, and professional
            - Give concise, actionable responses (2-4 sentences unless detail is requested)
            - When discussing jobs, mention specific job titles and companies from the available listings
            - If you don't know something, say so honestly
            - Do NOT use markdown formatting — plain text only
            - Do NOT make up job listings that don't exist in the provided context
            """;

    public ChatResponse chat(Long userId, String message, String sessionId) {
        if (!asiOneService.isAvailable()) {
            return ChatResponse.builder()
                    .reply("I'm sorry, the AI service is currently unavailable. Please try again later or browse jobs manually.")
                    .sessionId(sessionId)
                    .success(false)
                    .error("AI service not configured")
                    .build();
        }

        try {
            // Build context-aware system prompt
            String contextPrompt = buildContextPrompt(userId);

            // Get or create session history
            List<AsiOneRequest.Message> history = sessionHistory.computeIfAbsent(
                    sessionId, k -> new ArrayList<>());

            // Add system prompt if this is a new session
            if (history.isEmpty()) {
                history.add(AsiOneRequest.Message.builder()
                        .role("system").content(contextPrompt).build());
            }

            // Add user message
            history.add(AsiOneRequest.Message.builder()
                    .role("user").content(message).build());

            // Trim history if too long (keep system prompt + last N messages)
            if (history.size() > MAX_HISTORY_SIZE + 1) {
                AsiOneRequest.Message systemMsg = history.get(0);
                List<AsiOneRequest.Message> recent = new ArrayList<>();
                recent.add(systemMsg);
                recent.addAll(history.subList(history.size() - MAX_HISTORY_SIZE, history.size()));
                history.clear();
                history.addAll(recent);
            }

            // Call ASI:ONE with full history
            String reply = asiOneService.chatCompletionWithHistory(history, sessionId);

            if (reply == null) {
                return ChatResponse.builder()
                        .reply("I'm having trouble processing that right now. Could you try rephrasing?")
                        .sessionId(sessionId).success(false)
                        .error("Empty response from AI").build();
            }

            // Add assistant response to history
            history.add(AsiOneRequest.Message.builder()
                    .role("assistant").content(reply).build());

            // Generate suggestions
            List<String> suggestions = generateSuggestions(message);

            return ChatResponse.builder()
                    .reply(reply)
                    .sessionId(sessionId)
                    .suggestions(suggestions)
                    .success(true)
                    .build();

        } catch (Exception e) {
            log.error("Chatbot error: {}", e.getMessage());
            return ChatResponse.builder()
                    .reply("Something went wrong. Please try again.")
                    .sessionId(sessionId).success(false)
                    .error(e.getMessage()).build();
        }
    }

    public void clearSession(String sessionId) {
        sessionHistory.remove(sessionId);
        log.info("Cleared chat session: {}", sessionId);
    }

    private String buildContextPrompt(Long userId) {
        StringBuilder ctx = new StringBuilder(SYSTEM_PROMPT);

        // Add user's resume context if available
        if (userId != null) {
            ResumeProfile profile = recommendationService.getUserProfile(userId);
            if (profile != null && profile.getSkills() != null) {
                ctx.append("\n\nUser Profile:");
                ctx.append("\nSkills: ").append(profile.getSkills().stream()
                        .map(WeightedSkill::getName).collect(Collectors.joining(", ")));
                ctx.append("\nExperience Level: ").append(profile.getExperienceLevel());
                if (profile.getPreferredRoles() != null) {
                    ctx.append("\nPreferred Roles: ").append(String.join(", ", profile.getPreferredRoles()));
                }
            }
        }

        // Add available job listings context
        List<Job> jobs = jobRepository.findByStatus(JobStatusEnum.ACTIVE, PageRequest.of(0, 20)).getContent();
        if (!jobs.isEmpty()) {
            ctx.append("\n\nAvailable Job Listings:");
            for (Job j : jobs) {
                ctx.append(String.format("\n- %s at %s (%s) | Skills: %s",
                        j.getJobTitle(), j.getCompanyName(),
                        j.getEmploymentType() != null ? j.getEmploymentType().name() : "N/A",
                        j.getSkillsRequired() != null ? j.getSkillsRequired() : "Not specified"));
            }
        }

        return ctx.toString();
    }

    private List<String> generateSuggestions(String lastMessage) {
        String lower = lastMessage.toLowerCase();
        if (lower.contains("recommend") || lower.contains("suggest")) {
            return List.of("Show me remote jobs", "What skills should I learn?", "Help me with my resume");
        } else if (lower.contains("skill") || lower.contains("learn")) {
            return List.of("Show trending jobs", "What roles match my skills?", "Career path advice");
        } else if (lower.contains("interview") || lower.contains("prepare")) {
            return List.of("Common interview questions", "How to negotiate salary", "Show job recommendations");
        }
        return List.of("Show job recommendations", "Help with my resume", "Career advice");
    }
}
