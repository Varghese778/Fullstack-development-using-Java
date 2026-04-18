package com.jobportal.controller.web;

import com.jobportal.dto.request.ApplicationRequest;
import com.jobportal.dto.request.InterviewScheduleRequest;
import com.jobportal.entity.*;
import com.jobportal.enums.*;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class ApplicationWebController {

    private final ApplicationService applicationService;
    private final JobService jobService;
    private final ResumeService resumeService;

    @GetMapping("/apply/{jobId}")
    @PreAuthorize("hasRole('STUDENT')")
    public String applyForm(@PathVariable Long jobId, Model model) {
        Long userId = SecurityUtils.getCurrentUserId();
        model.addAttribute("job", jobService.getJobById(jobId));
        model.addAttribute("resumes", resumeService.getUserResumes(userId));
        model.addAttribute("applicationRequest", new ApplicationRequest());
        return "application/apply";
    }

    @PostMapping("/apply/{jobId}")
    @PreAuthorize("hasRole('STUDENT')")
    public String apply(@PathVariable Long jobId, @ModelAttribute ApplicationRequest request, RedirectAttributes redirectAttributes) {
        try {
            Long userId = SecurityUtils.getCurrentUserId();
            applicationService.submitApplication(userId, jobId, request);
            redirectAttributes.addFlashAttribute("message", "Application submitted successfully!");
            return "redirect:/my-applications";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/apply/" + jobId;
        }
    }

    @GetMapping("/my-applications")
    @PreAuthorize("hasRole('STUDENT')")
    public String myApplications(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(required = false) String status,
                                 Model model) {
        Long userId = SecurityUtils.getCurrentUserId();
        Page<Application> apps;
        if (status != null && !status.isBlank()) {
            apps = applicationService.getUserApplications(userId, PageRequest.of(page, 10, Sort.by("applicationDate").descending()));
        } else {
            apps = applicationService.getUserApplications(userId, PageRequest.of(page, 10, Sort.by("applicationDate").descending()));
        }
        model.addAttribute("applications", apps);
        return "application/my-applications";
    }

    @GetMapping("/applications/{id}")
    public String applicationDetail(@PathVariable Long id, Model model) {
        Application app = applicationService.getApplication(id);
        model.addAttribute("application", app);
        model.addAttribute("history", applicationService.getApplicationHistory(id));
        return "application/detail";
    }

    @PostMapping("/applications/{id}/withdraw")
    @PreAuthorize("hasRole('STUDENT')")
    public String withdrawApplication(@PathVariable Long id, @RequestParam(required = false) String reason,
                                      RedirectAttributes redirectAttributes) {
        Long userId = SecurityUtils.getCurrentUserId();
        applicationService.withdrawApplication(id, userId, reason);
        redirectAttributes.addFlashAttribute("message", "Application withdrawn.");
        return "redirect:/my-applications";
    }

    @GetMapping("/employer/applications")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String manageApplications(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(required = false) String status,
                                     Model model) {
        Long empId = SecurityUtils.getCurrentUserId();
        Page<Application> apps;
        if (status != null && !status.isBlank()) {
            apps = applicationService.getEmployerApplicationsByStatus(empId, ApplicationStatusEnum.valueOf(status),
                    PageRequest.of(page, 10, Sort.by("applicationDate").descending()));
            model.addAttribute("currentStatus", status);
        } else {
            apps = applicationService.getEmployerApplications(empId, PageRequest.of(page, 10, Sort.by("applicationDate").descending()));
        }
        model.addAttribute("applications", apps);
        return "application/manage";
    }

    @PostMapping("/employer/applications/{id}/shortlist")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String shortlist(@PathVariable Long id, @RequestParam(required = false) String notes,
                            RedirectAttributes redirectAttributes) {
        Long empId = SecurityUtils.getCurrentUserId();
        applicationService.shortlistApplication(id, empId, notes);
        redirectAttributes.addFlashAttribute("message", "Candidate shortlisted!");
        return "redirect:/employer/applications";
    }

    @PostMapping("/employer/applications/{id}/reject")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String reject(@PathVariable Long id, @RequestParam String reason,
                         RedirectAttributes redirectAttributes) {
        Long empId = SecurityUtils.getCurrentUserId();
        applicationService.rejectApplication(id, empId, RejectionReasonEnum.valueOf(reason), null);
        redirectAttributes.addFlashAttribute("message", "Candidate rejected.");
        return "redirect:/employer/applications";
    }

    @GetMapping("/employer/applications/{id}/schedule-interview")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String scheduleInterviewForm(@PathVariable Long id, Model model) {
        model.addAttribute("application", applicationService.getApplication(id));
        model.addAttribute("interviewRequest", new InterviewScheduleRequest());
        return "application/interview-schedule";
    }

    @PostMapping("/employer/applications/{id}/schedule-interview")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String scheduleInterview(@PathVariable Long id, @ModelAttribute InterviewScheduleRequest request,
                                    RedirectAttributes redirectAttributes) {
        Long empId = SecurityUtils.getCurrentUserId();
        applicationService.scheduleInterview(id, request, empId);
        redirectAttributes.addFlashAttribute("message", "Interview scheduled!");
        return "redirect:/employer/applications";
    }
}
