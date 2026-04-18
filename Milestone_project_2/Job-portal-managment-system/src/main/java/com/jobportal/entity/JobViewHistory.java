package com.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Tracks job detail page views for analytics.
 */
@Entity
@Table(name = "job_view_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobViewHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long viewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    @ToString.Exclude
    private Job job;

    private Long userId;

    private LocalDateTime viewedAt;

    private Integer timeSpent; // seconds
}
