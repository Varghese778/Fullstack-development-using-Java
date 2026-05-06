package com.jobportal.service.ai;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jobportal.dto.ai.ResumeProfile;
import com.jobportal.dto.ai.WeightedSkill;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

/**
 * Service for extracting text from PDF resumes and parsing them
 * using the ASI:ONE LLM for semantic understanding.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class ResumeParserService {

    private final AsiOneService asiOneService;
    private final ObjectMapper objectMapper;

    private static final String RESUME_PARSING_PROMPT = """
            You are an expert HR resume analyzer. Parse the following resume text and extract structured information.
            
            Return ONLY valid JSON (no markdown, no explanation, no code fences) in this exact format:
            {
              "skills": [
                {"name": "Java", "weight": 0.9, "category": "Programming Language"},
                {"name": "Spring Boot", "weight": 0.85, "category": "Framework"}
              ],
              "experiences": [
                "3 years as Software Developer at Company X working on microservices"
              ],
              "keywords": ["microservices", "REST API", "cloud"],
              "summary": "Experienced software developer with strong backend skills...",
              "experienceLevel": "MID",
              "totalYearsExperience": 3.0,
              "preferredRoles": ["Backend Developer", "Full Stack Developer"]
            }
            
            Rules:
            - weight: 0.0 to 1.0 based on prominence in resume (mentioned multiple times, project experience = higher)
            - experienceLevel: one of FRESHER, JUNIOR, MID, SENIOR, LEAD
            - category: one of "Programming Language", "Framework", "Database", "Cloud", "DevOps", "Tool", "Soft Skill", "Domain", "Other"
            - Be thorough: extract ALL skills mentioned, including implicit ones from project descriptions
            - Return ONLY the JSON object, nothing else
            """;

    /**
     * Extract raw text from a PDF file.
     */
    public String extractText(String filePath) {
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                log.error("Resume file not found: {}", filePath);
                return null;
            }

            try (PDDocument document = Loader.loadPDF(file)) {
                PDFTextStripper stripper = new PDFTextStripper();
                String text = stripper.getText(document);
                log.info("Extracted {} characters from PDF: {}", text.length(), filePath);
                return text;
            }
        } catch (IOException e) {
            log.error("Failed to extract text from PDF: {}", e.getMessage());
            return null;
        }
    }

    /**
     * Parse a resume using AI to extract structured profile information.
     */
    public ResumeProfile parseWithAI(String resumeText) {
        if (resumeText == null || resumeText.isBlank()) {
            log.warn("Empty resume text provided for AI parsing");
            return createEmptyProfile();
        }

        if (!asiOneService.isAvailable()) {
            log.warn("ASI:ONE API not available. Returning empty profile.");
            return createEmptyProfile();
        }

        try {
            // Truncate very long resumes to stay within token limits
            String truncatedText = resumeText.length() > 8000 ?
                    resumeText.substring(0, 8000) + "\n[... resume truncated ...]" : resumeText;

            String response = asiOneService.chatCompletion(RESUME_PARSING_PROMPT, truncatedText);

            if (response == null || response.isBlank()) {
                log.warn("ASI:ONE returned empty response for resume parsing");
                return createEmptyProfile();
            }

            // Clean the response - remove any markdown code fences if present
            String cleanedResponse = cleanJsonResponse(response);

            ResumeProfile profile = objectMapper.readValue(cleanedResponse, ResumeProfile.class);
            log.info("Successfully parsed resume: {} skills, {} experiences, level={}",
                    profile.getSkills() != null ? profile.getSkills().size() : 0,
                    profile.getExperiences() != null ? profile.getExperiences().size() : 0,
                    profile.getExperienceLevel());
            return profile;

        } catch (Exception e) {
            log.error("Failed to parse AI response as ResumeProfile: {}", e.getMessage());
            return createEmptyProfile();
        }
    }

    /**
     * Full pipeline: extract text from PDF and parse with AI.
     */
    public ResumeProfile parseResume(String filePath) {
        String text = extractText(filePath);
        if (text == null || text.isBlank()) {
            return createEmptyProfile();
        }
        return parseWithAI(text);
    }

    /**
     * Clean JSON response from potential markdown formatting.
     */
    private String cleanJsonResponse(String response) {
        String cleaned = response.trim();
        // Remove markdown code fences
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.substring(7);
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.substring(3);
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.substring(0, cleaned.length() - 3);
        }
        return cleaned.trim();
    }

    private ResumeProfile createEmptyProfile() {
        return ResumeProfile.builder()
                .skills(Collections.emptyList())
                .experiences(Collections.emptyList())
                .keywords(Collections.emptyList())
                .summary("Resume parsing unavailable")
                .experienceLevel("UNKNOWN")
                .totalYearsExperience(0.0)
                .preferredRoles(Collections.emptyList())
                .build();
    }
}
