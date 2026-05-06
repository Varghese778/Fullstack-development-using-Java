package com.jobportal.dto.ai;

import lombok.*;
import java.util.List;

/**
 * Represents a job recommendation with relevance scoring and explanation.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobRecommendation {

    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String companyLogo;
    private String department;
    private String employmentType;
    private String experienceLevel;
    private Double salaryMin;
    private Double salaryMax;
    private String remotePolicy;
    private String jobDescription;
    private Integer viewCount;

    /**
     * Relevance score from 0.0 to 1.0 (1.0 = perfect match).
     */
    private double relevanceScore;

    /**
     * Percentage representation of relevance (0-100).
     */
    private int matchPercentage;

    /**
     * Skills from the user's resume that match this job.
     */
    private List<String> matchedSkills;

    /**
     * Skills required by the job that the user lacks.
     */
    private List<String> missingSkills;

    /**
     * AI-generated explanation: "Why this job matches you".
     */
    private String explanation;
}
