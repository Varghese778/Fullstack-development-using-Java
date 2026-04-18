package com.jobportal.repository;

import com.jobportal.entity.NotificationTemplate;
import com.jobportal.enums.NotificationTypeEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface NotificationTemplateRepository extends JpaRepository<NotificationTemplate, Long> {
    Optional<NotificationTemplate> findByTemplateName(String templateName);
    Optional<NotificationTemplate> findByNotificationType(NotificationTypeEnum type);
}
