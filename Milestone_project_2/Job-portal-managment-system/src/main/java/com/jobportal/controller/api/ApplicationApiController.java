package com.jobportal.controller.api;

import com.jobportal.dto.request.ApplicationRequest;
import com.jobportal.dto.request.InterviewScheduleRequest;
import com.jobportal.dto.response.ApplicationResponse;
import com.jobportal.dto.shared.ApiResponse;
import com.jobportal.enums.*;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationApiController {

    private final ApplicationService applicationService;

    @PostMapping
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<ApplicationResponse>> submit(@RequestParam Long jobId, @RequestBody ApplicationRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success("Applied", applicationService.toResponse(applicationService.submitApplication(userId, jobId, request))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ApplicationResponse>> getApplication(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(applicationService.toResponse(applicationService.getApplication(id))));
    }

    @GetMapping("/my")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> myApplications(@RequestParam(defaultValue = "0") int page) {
        Long userId = SecurityUtils.getCurrentUserId();
        Page<ApplicationResponse> apps = applicationService.getUserApplications(userId, PageRequest.of(page, 10, Sort.by("applicationDate").descending()))
                .map(applicationService::toResponse);
        return ResponseEntity.ok(ApiResponse.success(apps));
    }

    @GetMapping("/job/{jobId}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<Page<ApplicationResponse>>> jobApplications(@PathVariable Long jobId, @RequestParam(defaultValue = "0") int page) {
        Page<ApplicationResponse> apps = applicationService.getJobApplications(jobId, PageRequest.of(page, 10))
                .map(applicationService::toResponse);
        return ResponseEntity.ok(ApiResponse.success(apps));
    }

    @PostMapping("/{id}/shortlist")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<String>> shortlist(@PathVariable Long id, @RequestParam(required = false) String notes) {
        applicationService.shortlistApplication(id, SecurityUtils.getCurrentUserId(), notes);
        return ResponseEntity.ok(ApiResponse.success("Shortlisted"));
    }

    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<String>> reject(@PathVariable Long id, @RequestParam String reason) {
        applicationService.rejectApplication(id, SecurityUtils.getCurrentUserId(), RejectionReasonEnum.valueOf(reason), null);
        return ResponseEntity.ok(ApiResponse.success("Rejected"));
    }

    @PostMapping("/{id}/withdraw")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<String>> withdraw(@PathVariable Long id, @RequestParam(required = false) String reason) {
        applicationService.withdrawApplication(id, SecurityUtils.getCurrentUserId(), reason);
        return ResponseEntity.ok(ApiResponse.success("Withdrawn"));
    }

    @PostMapping("/{id}/schedule-interview")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<String>> scheduleInterview(@PathVariable Long id, @RequestBody InterviewScheduleRequest request) {
        applicationService.scheduleInterview(id, request, SecurityUtils.getCurrentUserId());
        return ResponseEntity.ok(ApiResponse.success("Interview scheduled"));
    }

    @PostMapping("/bulk-update-status")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<String>> bulkUpdate(@RequestBody List<Long> ids, @RequestParam String status) {
        applicationService.bulkUpdateStatus(ids, ApplicationStatusEnum.valueOf(status), SecurityUtils.getCurrentUserId());
        return ResponseEntity.ok(ApiResponse.success("Bulk update complete"));
    }
}
