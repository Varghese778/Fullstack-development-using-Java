package com.jobportal.repository;

import com.jobportal.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
    Page<Notification> findByRecipientIdAndRecipientTypeOrderByCreatedAtDesc(Long recipientId, String recipientType, Pageable pageable);
    List<Notification> findByRecipientIdAndRecipientTypeAndIsReadOrderByCreatedAtDesc(Long recipientId, String recipientType, Boolean isRead);
    long countByRecipientIdAndRecipientTypeAndIsRead(Long recipientId, String recipientType, Boolean isRead);
    List<Notification> findByStatusAndRetryCountLessThan(String status, Integer maxRetry);
}
