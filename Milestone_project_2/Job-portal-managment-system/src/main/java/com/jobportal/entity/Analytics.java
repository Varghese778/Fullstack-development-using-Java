package com.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Pre-aggregated analytics metrics for dashboards.
 */
@Entity
@Table(name = "analytics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Analytics {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analyticsId;

    private String analyticsType; // DAILY_JOB_SEEKER, DAILY_EMPLOYER, DAILY_ADMIN

    private LocalDate date;

    private Long userId;

    private Long employerId;

    @Builder.Default
    private Integer totalUsers = 0;
    @Builder.Default
    private Integer newUsers = 0;
    @Builder.Default
    private Integer activeUsers = 0;
    @Builder.Default
    private Integer totalJobs = 0;
    @Builder.Default
    private Integer newJobs = 0;
    @Builder.Default
    private Integer totalApplications = 0;
    @Builder.Default
    private Integer newApplications = 0;
    @Builder.Default
    private Integer shortlistedApplications = 0;
    @Builder.Default
    private Integer rejectedApplications = 0;
    @Builder.Default
    private Integer hirings = 0;

    @Column(length = 2000)
    private String customMetrics; // JSON

    @CreationTimestamp
    private LocalDateTime createdAt;
}
