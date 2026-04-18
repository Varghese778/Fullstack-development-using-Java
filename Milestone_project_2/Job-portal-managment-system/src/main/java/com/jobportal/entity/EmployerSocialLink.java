package com.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents an employer's social media link.
 */
@Entity
@Table(name = "employer_social_links")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployerSocialLink {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long linkId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    @ToString.Exclude
    private Employer employer;

    private String platform; // LINKEDIN, TWITTER, FACEBOOK, INSTAGRAM

    private String url;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
