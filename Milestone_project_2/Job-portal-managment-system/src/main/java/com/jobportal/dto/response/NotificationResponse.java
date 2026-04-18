package com.jobportal.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class NotificationResponse {
    private Long notificationId;
    private String notificationType;
    private String subject;
    private String message;
    private String actionLink;
    private String priority;
    private Boolean isRead;
    private LocalDateTime createdAt;
}
