package com.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a team member (HR staff, recruiter) within an employer's account.
 */
@Entity
@Table(name = "team_members")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    @ToString.Exclude
    private Employer employer;

    private String email;

    private String name;

    private String role; // RECRUITER, MANAGER, ADMIN

    @Column(length = 1000)
    private String permissions; // JSON array

    @CreationTimestamp
    private LocalDateTime joinedAt;

    @Builder.Default
    private Boolean isActive = true;
}
