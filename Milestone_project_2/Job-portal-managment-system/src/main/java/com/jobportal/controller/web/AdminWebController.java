package com.jobportal.controller.web;

import com.jobportal.enums.ApprovalStatusEnum;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
@PreAuthorize("hasRole('ADMIN')")
public class AdminWebController {

    private final AnalyticsService analyticsService;
    private final UserService userService;
    private final EmployerService employerService;
    private final JobService jobService;
    private final AuditLogService auditLogService;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("stats", analyticsService.getAdminDashboard());
        return "admin/dashboard";
    }

    @GetMapping("/users")
    public String manageUsers(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(required = false) String keyword,
                              Model model) {
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("users", userService.searchUsers(keyword, PageRequest.of(page, 20, Sort.by("createdAt").descending())));
            model.addAttribute("keyword", keyword);
        } else {
            model.addAttribute("users", userService.getAllUsers(PageRequest.of(page, 20, Sort.by("createdAt").descending())));
        }
        return "admin/users";
    }

    @PostMapping("/users/{id}/deactivate")
    public String deactivateUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.deactivateUser(id);
        redirectAttributes.addFlashAttribute("message", "User deactivated.");
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/activate")
    public String activateUser(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        userService.activateUser(id);
        redirectAttributes.addFlashAttribute("message", "User activated.");
        return "redirect:/admin/users";
    }

    @GetMapping("/employers")
    public String manageEmployers(@RequestParam(defaultValue = "0") int page,
                                  @RequestParam(required = false) String status,
                                  Model model) {
        if (status != null && !status.isBlank()) {
            model.addAttribute("employers", employerService.getEmployersByStatus(ApprovalStatusEnum.valueOf(status), PageRequest.of(page, 20)));
            model.addAttribute("currentStatus", status);
        } else {
            model.addAttribute("employers", employerService.getAllEmployers(PageRequest.of(page, 20)));
        }
        return "admin/employers";
    }

    @PostMapping("/employers/{id}/verify")
    public String verifyEmployer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Long adminId = SecurityUtils.getCurrentUserId();
        employerService.verifyEmployer(id, adminId);
        redirectAttributes.addFlashAttribute("message", "Employer verified!");
        return "redirect:/admin/employers";
    }

    @PostMapping("/employers/{id}/reject")
    public String rejectEmployer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Long adminId = SecurityUtils.getCurrentUserId();
        employerService.rejectEmployer(id, adminId);
        redirectAttributes.addFlashAttribute("message", "Employer rejected.");
        return "redirect:/admin/employers";
    }

    @GetMapping("/jobs")
    public String manageJobs(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("jobs", jobService.getActiveJobs(PageRequest.of(page, 20, Sort.by("createdAt").descending())));
        return "admin/jobs";
    }

    @GetMapping("/audit-logs")
    public String auditLogs(@RequestParam(defaultValue = "0") int page, Model model) {
        model.addAttribute("logs", auditLogService.getAuditLogs(PageRequest.of(page, 50)));
        return "admin/audit-logs";
    }
}
