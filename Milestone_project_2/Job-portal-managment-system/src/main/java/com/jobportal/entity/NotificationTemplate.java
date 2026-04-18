package com.jobportal.entity;

import com.jobportal.enums.NotificationTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Email / notification template with variable placeholders.
 */
@Entity
@Table(name = "notification_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long templateId;

    @Column(unique = true)
    private String templateName;

    @Enumerated(EnumType.STRING)
    private NotificationTypeEnum notificationType;

    private String subject;

    @Column(length = 5000)
    private String plainTextContent;

    @Column(length = 10000)
    private String htmlContent;

    @Column(length = 1000)
    private String variables; // JSON array of placeholder names

    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
