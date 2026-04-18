package com.jobportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Represents a user's work experience record.
 */
@Entity
@Table(name = "user_experience")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long experienceId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @NotNull
    private String jobTitle;

    @NotNull
    private String company;

    private LocalDate startDate;

    private LocalDate endDate;

    @Builder.Default
    private Boolean currentlyWorking = false;

    @Column(length = 1000)
    private String description;

    private String skills;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
