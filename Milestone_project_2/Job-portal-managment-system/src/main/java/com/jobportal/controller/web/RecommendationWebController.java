package com.jobportal.controller.web;

import com.jobportal.dto.ai.JobRecommendation;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.ai.JobRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecommendationWebController {

    private final JobRecommendationService recommendationService;

    @GetMapping("/recommendations")
    @PreAuthorize("hasRole('STUDENT')")
    public String recommendations(Model model) {
        Long userId = SecurityUtils.getCurrentUserId();
        List<JobRecommendation> recommendations = recommendationService.getRecommendations(userId, 12);
        model.addAttribute("recommendations", recommendations);
        return "job/recommendations";
    }
}
