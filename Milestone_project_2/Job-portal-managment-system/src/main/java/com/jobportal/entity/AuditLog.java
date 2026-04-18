package com.jobportal.entity;

import com.jobportal.enums.AuditActionEnum;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Security audit log for tracking user actions.
 */
@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private AuditActionEnum action;

    private String resourceType;

    private Long resourceId;

    private String result; // SUCCESS, FAILURE

    @Column(length = 500)
    private String errorMessage;

    private String ipAddress;

    private String userAgent;

    private LocalDateTime timestamp;

    @Column(length = 2000)
    private String details; // JSON
}
