package com.jobportal.entity;

import com.jobportal.enums.ProficiencyLevelEnum;
import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a skill required for a job posting.
 */
@Entity
@Table(name = "job_skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    @ToString.Exclude
    private Job job;

    private String skillName;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProficiencyLevelEnum proficiencyLevel = ProficiencyLevelEnum.INTERMEDIATE;

    @Builder.Default
    private Boolean isRequired = true;
}
