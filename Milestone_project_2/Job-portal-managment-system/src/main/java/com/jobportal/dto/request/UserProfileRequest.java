package com.jobportal.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class UserProfileRequest {
    @Size(max = 500) private String bio;
    private String phoneNumber;
    private String location;
    private String portfolioUrl;
    private String linkedinUrl;
    private String githubUrl;
    private Double expectedSalary;
    private String availabilityStatus;
}
