package com.jobportal.entity;

import com.jobportal.enums.NotificationTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Stores per-user notification preferences.
 */
@Entity
@Table(name = "notification_preferences")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NotificationPreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preferenceId;

    private Long userId;

    @Enumerated(EnumType.STRING)
    private NotificationTypeEnum notificationType;

    @Builder.Default
    private Boolean emailEnabled = true;

    @Builder.Default
    private Boolean inAppEnabled = true;

    @Builder.Default
    private String emailFrequency = "IMMEDIATELY"; // IMMEDIATELY, DAILY_DIGEST, WEEKLY_DIGEST, NONE

    private LocalTime digestTime;

    private LocalTime dndStartTime;

    private LocalTime dndEndTime;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
