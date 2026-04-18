package com.jobportal.entity;

import com.jobportal.enums.ApplicationStatusEnum;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Audit trail for application status changes.
 */
@Entity
@Table(name = "application_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApplicationHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    @ToString.Exclude
    private Application application;

    @Enumerated(EnumType.STRING)
    private ApplicationStatusEnum oldStatus;

    @Enumerated(EnumType.STRING)
    private ApplicationStatusEnum newStatus;

    private Long changedBy;

    private LocalDateTime changedAt;

    @Column(length = 500)
    private String changeReason;

    @Column(length = 500)
    private String notes;
}
