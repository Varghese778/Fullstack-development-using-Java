package com.jobportal.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDate;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ExperienceRequest {
    @NotBlank(message = "Job title is required") private String jobTitle;
    @NotBlank(message = "Company is required") private String company;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean currentlyWorking;
    private String description;
    private String skills;
}
