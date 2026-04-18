package com.jobportal.controller.api;

import com.jobportal.dto.response.NotificationResponse;
import com.jobportal.dto.shared.ApiResponse;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationApiController {

    private final NotificationService notificationService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<NotificationResponse>>> list(@RequestParam(defaultValue = "0") int page) {
        Long userId = SecurityUtils.getCurrentUserId();
        String type = SecurityUtils.hasRole("EMPLOYER") ? "EMPLOYER" : "USER";
        Page<NotificationResponse> notifs = notificationService.getNotifications(userId, type,
                PageRequest.of(page, 20, Sort.by("createdAt").descending())).map(notificationService::toResponse);
        return ResponseEntity.ok(ApiResponse.success(notifs));
    }

    @GetMapping("/unread-count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> unreadCount() {
        Long userId = SecurityUtils.getCurrentUserId();
        String type = SecurityUtils.hasRole("EMPLOYER") ? "EMPLOYER" : "USER";
        long count = notificationService.getUnreadCount(userId, type);
        return ResponseEntity.ok(ApiResponse.success(Map.of("count", count)));
    }

    @PostMapping("/{id}/mark-read")
    public ResponseEntity<ApiResponse<String>> markRead(@PathVariable Long id) {
        notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Marked as read"));
    }

    @PostMapping("/mark-all-read")
    public ResponseEntity<ApiResponse<String>> markAllRead() {
        Long userId = SecurityUtils.getCurrentUserId();
        String type = SecurityUtils.hasRole("EMPLOYER") ? "EMPLOYER" : "USER";
        notificationService.markAllAsRead(userId, type);
        return ResponseEntity.ok(ApiResponse.success("All marked as read"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted"));
    }
}
