package com.jobportal.controller.web;

import com.jobportal.dto.request.JobPostRequest;
import com.jobportal.entity.Job;
import com.jobportal.enums.JobStatusEnum;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.JobService;
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
public class JobWebController {

    private final JobService jobService;

    @GetMapping("/jobs")
    public String listJobs(@RequestParam(defaultValue = "0") int page,
                           @RequestParam(defaultValue = "12") int size,
                           @RequestParam(required = false) String keyword,
                           Model model) {
        Page<Job> jobs;
        if (keyword != null && !keyword.isBlank()) {
            jobs = jobService.searchJobs(keyword, PageRequest.of(page, size, Sort.by("createdAt").descending()));
            model.addAttribute("keyword", keyword);
        } else {
            jobs = jobService.getActiveJobs(PageRequest.of(page, size, Sort.by("createdAt").descending()));
        }
        model.addAttribute("jobs", jobs);
        model.addAttribute("trending", jobService.getTrendingJobs(5));
        return "job/list";
    }

    @GetMapping("/jobs/{id}")
    public String jobDetail(@PathVariable Long id, Model model) {
        Job job = jobService.getJobById(id);
        Long userId = SecurityUtils.getCurrentUserId();
        if (userId != null) {
            jobService.incrementViewCount(id, userId);
        }
        model.addAttribute("job", job);
        return "job/detail";
    }

    @GetMapping("/jobs/post")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String postJobForm(Model model) {
        model.addAttribute("jobRequest", new JobPostRequest());
        return "job/post";
    }

    @PostMapping("/jobs/post")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String postJob(@ModelAttribute JobPostRequest request, RedirectAttributes redirectAttributes) {
        Long empId = SecurityUtils.getCurrentUserId();
        Job job = jobService.createJob(empId, request);
        redirectAttributes.addFlashAttribute("message", "Job posted as draft! Publish it to make it visible.");
        return "redirect:/employer/my-jobs";
    }

    @GetMapping("/jobs/{id}/edit")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String editJobForm(@PathVariable Long id, Model model) {
        model.addAttribute("job", jobService.getJobById(id));
        return "job/edit";
    }

    @PostMapping("/jobs/{id}/edit")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String updateJob(@PathVariable Long id, @ModelAttribute JobPostRequest request, RedirectAttributes redirectAttributes) {
        jobService.updateJob(id, request);
        redirectAttributes.addFlashAttribute("message", "Job updated successfully!");
        return "redirect:/employer/my-jobs";
    }

    @PostMapping("/jobs/{id}/publish")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String publishJob(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        jobService.publishJob(id);
        redirectAttributes.addFlashAttribute("message", "Job published and is now visible!");
        return "redirect:/employer/my-jobs";
    }

    @PostMapping("/jobs/{id}/close")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String closeJob(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        jobService.closeJob(id);
        redirectAttributes.addFlashAttribute("message", "Job closed.");
        return "redirect:/employer/my-jobs";
    }

    @PostMapping("/jobs/{id}/archive")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String archiveJob(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        jobService.archiveJob(id);
        redirectAttributes.addFlashAttribute("message", "Job archived.");
        return "redirect:/employer/my-jobs";
    }

    @GetMapping("/employer/my-jobs")
    @PreAuthorize("hasRole('EMPLOYER')")
    public String myJobs(@RequestParam(defaultValue = "0") int page,
                         @RequestParam(required = false) String status,
                         Model model) {
        Long empId = SecurityUtils.getCurrentUserId();
        Page<Job> jobs;
        if (status != null && !status.isBlank()) {
            jobs = jobService.getEmployerJobsByStatus(empId, JobStatusEnum.valueOf(status), PageRequest.of(page, 10, Sort.by("createdAt").descending()));
            model.addAttribute("currentStatus", status);
        } else {
            jobs = jobService.getEmployerJobs(empId, PageRequest.of(page, 10, Sort.by("createdAt").descending()));
        }
        model.addAttribute("jobs", jobs);
        return "job/my-jobs";
    }

    @GetMapping("/jobs/search")
    public String searchJobs(@RequestParam(required = false) String keyword,
                             @RequestParam(defaultValue = "0") int page,
                             Model model) {
        if (keyword != null && !keyword.isBlank()) {
            model.addAttribute("jobs", jobService.searchJobs(keyword, PageRequest.of(page, 12)));
            model.addAttribute("keyword", keyword);
        }
        return "job/search";
    }
}
