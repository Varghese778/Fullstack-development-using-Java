package com.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Tracks login attempts for security (account lockout after failures).
 */
@Entity
@Table(name = "login_attempts")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginAttempt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attemptId;

    private String email;

    private Boolean isSuccess;

    private String ipAddress;

    private String userAgent;

    private LocalDateTime attemptTime;

    private String failureReason;
}
