package com.jobportal.service;

import com.jobportal.dto.response.NotificationResponse;
import com.jobportal.entity.*;
import com.jobportal.enums.*;
import com.jobportal.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationPreferenceRepository preferenceRepository;
    private final JavaMailSender mailSender;

    public Notification createNotification(Long recipientId, String recipientType,
                                           NotificationTypeEnum type, String subject, String message,
                                           String actionLink, NotificationPriorityEnum priority) {
        Notification notification = Notification.builder()
                .recipientId(recipientId).recipientType(recipientType)
                .notificationType(type).subject(subject).message(message)
                .actionLink(actionLink).priority(priority != null ? priority : NotificationPriorityEnum.NORMAL)
                .build();
        return notificationRepository.save(notification);
    }

    @Async
    public void sendEmailNotification(String toEmail, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            message.setFrom("noreply@jobportal.com");
            mailSender.send(message);
            log.info("Email sent to: {}", toEmail);
        } catch (Exception e) {
            log.warn("Email sending failed to {}: {} (This is expected if SMTP is not configured)", toEmail, e.getMessage());
        }
    }

    public Page<Notification> getNotifications(Long recipientId, String recipientType, Pageable pageable) {
        return notificationRepository.findByRecipientIdAndRecipientTypeOrderByCreatedAtDesc(recipientId, recipientType, pageable);
    }

    public List<Notification> getUnreadNotifications(Long recipientId, String recipientType) {
        return notificationRepository.findByRecipientIdAndRecipientTypeAndIsReadOrderByCreatedAtDesc(recipientId, recipientType, false);
    }

    public long getUnreadCount(Long recipientId, String recipientType) {
        return notificationRepository.countByRecipientIdAndRecipientTypeAndIsRead(recipientId, recipientType, false);
    }

    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(n -> {
            n.setIsRead(true);
            n.setReadAt(LocalDateTime.now());
            notificationRepository.save(n);
        });
    }

    public void markAllAsRead(Long recipientId, String recipientType) {
        List<Notification> unread = notificationRepository
                .findByRecipientIdAndRecipientTypeAndIsReadOrderByCreatedAtDesc(recipientId, recipientType, false);
        unread.forEach(n -> {
            n.setIsRead(true);
            n.setReadAt(LocalDateTime.now());
        });
        notificationRepository.saveAll(unread);
    }

    public void deleteNotification(Long notificationId) {
        notificationRepository.deleteById(notificationId);
    }

    public NotificationResponse toResponse(Notification n) {
        return NotificationResponse.builder()
                .notificationId(n.getNotificationId())
                .notificationType(n.getNotificationType() != null ? n.getNotificationType().name() : null)
                .subject(n.getSubject()).message(n.getMessage())
                .actionLink(n.getActionLink())
                .priority(n.getPriority() != null ? n.getPriority().name() : null)
                .isRead(n.getIsRead()).createdAt(n.getCreatedAt()).build();
    }

    // Retry failed emails every 5 minutes
    @Scheduled(fixedDelay = 300000)
    public void retryFailedEmails() {
        List<Notification> failed = notificationRepository.findByStatusAndRetryCountLessThan("FAILED", 3);
        failed.forEach(n -> {
            log.info("Retrying email for notification {}", n.getNotificationId());
            n.setRetryCount(n.getRetryCount() + 1);
            n.setStatus("PENDING");
            notificationRepository.save(n);
        });
    }
}
