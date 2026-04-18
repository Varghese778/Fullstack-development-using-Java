package com.jobportal.entity;

import com.jobportal.enums.ProficiencyLevelEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a skill tag associated with a user.
 */
@Entity
@Table(name = "user_skills")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSkill {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long skillId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @NotNull
    private String skillName;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ProficiencyLevelEnum proficiencyLevel = ProficiencyLevelEnum.BEGINNER;

    @Builder.Default
    private Integer endorsementCount = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
