package com.jobportal.entity;

import com.jobportal.enums.NotificationPriorityEnum;
import com.jobportal.enums.NotificationTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a notification sent to a user or employer.
 */
@Entity
@Table(name = "notifications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notificationId;

    private Long recipientId;

    private String recipientType; // USER, EMPLOYER, ADMIN

    @Enumerated(EnumType.STRING)
    private NotificationTypeEnum notificationType;

    private String subject;

    @Column(length = 2000)
    private String message;

    @Column(length = 5000)
    private String htmlContent;

    private String actionLink;

    private String relatedEntityType;

    private Long relatedEntityId;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private NotificationPriorityEnum priority = NotificationPriorityEnum.NORMAL;

    @Builder.Default
    private String status = "PENDING"; // PENDING, SENT, FAILED, BOUNCED

    @Builder.Default
    private Boolean isRead = false;

    private LocalDateTime readAt;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private LocalDateTime sentAt;

    private String failureReason;

    @Builder.Default
    private Integer retryCount = 0;

    private LocalDateTime nextRetryTime;
}
