package com.jobportal.controller.api;

import com.jobportal.dto.ai.JobRecommendation;
import com.jobportal.dto.shared.ApiResponse;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.ai.JobRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationApiController {

    private final JobRecommendationService recommendationService;

    @GetMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<List<JobRecommendation>>> getRecommendations(
            @RequestParam(defaultValue = "10") int limit) {
        Long userId = SecurityUtils.getCurrentUserId();
        List<JobRecommendation> recs = recommendationService.getRecommendations(userId, limit);
        return ResponseEntity.ok(ApiResponse.success(recs));
    }

    @GetMapping("/{jobId}/explain")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<String>> getExplanation(@PathVariable Long jobId) {
        Long userId = SecurityUtils.getCurrentUserId();
        String explanation = recommendationService.getExplanation(userId, jobId);
        return ResponseEntity.ok(ApiResponse.success(explanation));
    }
}
