package com.jobportal.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class JobPostRequest {
    @NotBlank(message = "Job title is required")
    @Size(min = 3, max = 100) private String jobTitle;

    @NotBlank(message = "Job description is required")
    @Size(min = 50, max = 5000) private String jobDescription;

    private String department;
    private String category;
    private Double salaryMin;
    private Double salaryMax;
    private String experienceLevel;
    private String employmentType;
    private String remotePolicy;
    private Integer numberOfPositions;
    private LocalDate applicationDeadline;
    private String benefits;
    private String skillsRequired;
    private String reportingManager;
    private String externalJobUrl;
    private List<String> locations;
    private List<String> requiredSkills;
}
