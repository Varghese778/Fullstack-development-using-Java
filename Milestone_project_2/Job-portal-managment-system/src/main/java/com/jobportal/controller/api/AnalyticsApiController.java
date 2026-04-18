package com.jobportal.controller.api;

import com.jobportal.dto.response.DashboardStatsResponse;
import com.jobportal.dto.shared.ApiResponse;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/analytics")
@RequiredArgsConstructor
public class AnalyticsApiController {

    private final AnalyticsService analyticsService;

    @GetMapping("/dashboard/student")
    @PreAuthorize("hasRole('STUDENT')")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> studentDashboard() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getStudentDashboard(userId)));
    }

    @GetMapping("/dashboard/employer")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> employerDashboard() {
        Long empId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getEmployerDashboard(empId)));
    }

    @GetMapping("/dashboard/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<DashboardStatsResponse>> adminDashboard() {
        return ResponseEntity.ok(ApiResponse.success(analyticsService.getAdminDashboard()));
    }
}
