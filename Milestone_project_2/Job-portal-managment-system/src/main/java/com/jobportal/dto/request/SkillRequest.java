package com.jobportal.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class SkillRequest {
    @NotBlank(message = "Skill name is required") private String skillName;
    private String proficiencyLevel;
}
