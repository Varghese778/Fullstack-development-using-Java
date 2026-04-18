package com.jobportal.controller.web;

import com.jobportal.security.SecurityUtils;
import com.jobportal.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/notifications")
@RequiredArgsConstructor
public class NotificationWebController {

    private final NotificationService notificationService;

    @GetMapping
    public String notificationCenter(@RequestParam(defaultValue = "0") int page, Model model) {
        Long userId = SecurityUtils.getCurrentUserId();
        String type = SecurityUtils.hasRole("EMPLOYER") ? "EMPLOYER" : "USER";
        model.addAttribute("notifications", notificationService.getNotifications(userId, type, PageRequest.of(page, 20, Sort.by("createdAt").descending())));
        model.addAttribute("unreadCount", notificationService.getUnreadCount(userId, type));
        return "notification/center";
    }

    @PostMapping("/{id}/mark-read")
    public String markRead(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        notificationService.markAsRead(id);
        return "redirect:/notifications";
    }

    @PostMapping("/mark-all-read")
    public String markAllRead(RedirectAttributes redirectAttributes) {
        Long userId = SecurityUtils.getCurrentUserId();
        String type = SecurityUtils.hasRole("EMPLOYER") ? "EMPLOYER" : "USER";
        notificationService.markAllAsRead(userId, type);
        redirectAttributes.addFlashAttribute("message", "All notifications marked as read.");
        return "redirect:/notifications";
    }

    @PostMapping("/{id}/delete")
    public String deleteNotification(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        notificationService.deleteNotification(id);
        redirectAttributes.addFlashAttribute("message", "Notification deleted.");
        return "redirect:/notifications";
    }
}
