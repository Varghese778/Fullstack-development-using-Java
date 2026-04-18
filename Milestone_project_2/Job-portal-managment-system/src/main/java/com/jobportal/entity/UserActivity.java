package com.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Tracks user activity for analytics and audit.
 */
@Entity
@Table(name = "user_activities")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long activityId;

    private Long userId;

    private String activityType; // LOGIN, PROFILE_UPDATE, JOB_VIEW, JOB_APPLY, RESUME_UPLOAD, etc.

    private String resource;

    private LocalDateTime timestamp;

    private String sessionId;

    private String ipAddress;

    private String userAgent;
}
