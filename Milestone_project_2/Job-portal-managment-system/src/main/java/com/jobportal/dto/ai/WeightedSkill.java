package com.jobportal.dto.ai;

import lombok.*;

/**
 * Represents a skill extracted from a resume with a relevance weight.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeightedSkill {

    private String name;

    /**
     * Weight between 0.0 and 1.0 indicating how prominent
     * this skill is in the resume.
     */
    private double weight;

    /**
     * Category such as "Programming Language", "Framework",
     * "Database", "Cloud", "Soft Skill", etc.
     */
    private String category;
}
