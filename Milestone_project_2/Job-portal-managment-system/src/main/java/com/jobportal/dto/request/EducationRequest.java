package com.jobportal.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EducationRequest {
    @NotBlank(message = "Degree is required") private String degree;
    @NotBlank(message = "Institution is required") private String institution;
    private String fieldOfStudy;
    private Integer graduationYear;
    private String description;
}
