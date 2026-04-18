package com.jobportal.controller.api;

import com.jobportal.dto.response.ResumeResponse;
import com.jobportal.dto.shared.ApiResponse;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@PreAuthorize("hasRole('STUDENT')")
public class ResumeApiController {

    private final ResumeService resumeService;

    @PostMapping("/upload")
    public ResponseEntity<ApiResponse<ResumeResponse>> upload(@RequestParam MultipartFile file,
                                                               @RequestParam(defaultValue = "false") boolean isPrimary) {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success("Uploaded", resumeService.toResponse(resumeService.uploadResume(userId, file, isPrimary))));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ResumeResponse>>> list() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(resumeService.getUserResumes(userId)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ResumeResponse>> get(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(resumeService.toResponse(resumeService.getResume(id))));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> delete(@PathVariable Long id) {
        resumeService.deleteResume(id);
        return ResponseEntity.ok(ApiResponse.success("Deleted"));
    }

    @PostMapping("/{id}/set-primary")
    public ResponseEntity<ApiResponse<String>> setPrimary(@PathVariable Long id) {
        Long userId = SecurityUtils.getCurrentUserId();
        resumeService.setPrimaryResume(userId, id);
        return ResponseEntity.ok(ApiResponse.success("Primary set"));
    }
}
