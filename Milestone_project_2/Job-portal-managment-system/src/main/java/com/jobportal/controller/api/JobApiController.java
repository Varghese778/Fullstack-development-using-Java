package com.jobportal.controller.api;

import com.jobportal.dto.request.JobPostRequest;
import com.jobportal.dto.response.JobResponse;
import com.jobportal.dto.shared.ApiResponse;
import com.jobportal.entity.Job;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.JobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/jobs")
@RequiredArgsConstructor
public class JobApiController {

    private final JobService jobService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<JobResponse>> createJob(@Valid @RequestBody JobPostRequest request) {
        Long empId = SecurityUtils.getCurrentUserId();
        Job job = jobService.createJob(empId, request);
        return ResponseEntity.ok(ApiResponse.success("Job created", jobService.toResponse(job)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<JobResponse>> getJob(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(jobService.toResponse(jobService.getJobById(id))));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<JobResponse>> updateJob(@PathVariable Long id, @Valid @RequestBody JobPostRequest request) {
        Job job = jobService.updateJob(id, request);
        return ResponseEntity.ok(ApiResponse.success("Job updated", jobService.toResponse(job)));
    }

    @PostMapping("/{id}/publish")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<String>> publishJob(@PathVariable Long id) {
        jobService.publishJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job published"));
    }

    @PostMapping("/{id}/close")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<String>> closeJob(@PathVariable Long id) {
        jobService.closeJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job closed"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<String>> archiveJob(@PathVariable Long id) {
        jobService.archiveJob(id);
        return ResponseEntity.ok(ApiResponse.success("Job archived"));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<JobResponse>>> listJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size,
            @RequestParam(required = false) String keyword) {
        Page<JobResponse> jobs;
        if (keyword != null && !keyword.isBlank()) {
            jobs = jobService.searchJobs(keyword, PageRequest.of(page, size, Sort.by("createdAt").descending())).map(jobService::toResponse);
        } else {
            jobs = jobService.getActiveJobs(PageRequest.of(page, size, Sort.by("createdAt").descending())).map(jobService::toResponse);
        }
        return ResponseEntity.ok(ApiResponse.success(jobs));
    }

    @GetMapping("/trending")
    public ResponseEntity<ApiResponse<List<JobResponse>>> getTrending() {
        List<JobResponse> trending = jobService.getTrendingJobs(10).stream().map(jobService::toResponse).collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponse.success(trending));
    }
}
