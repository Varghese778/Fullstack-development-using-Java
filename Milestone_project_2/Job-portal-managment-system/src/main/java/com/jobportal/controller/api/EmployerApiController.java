package com.jobportal.controller.api;

import com.jobportal.dto.request.*;
import com.jobportal.dto.response.EmployerResponse;
import com.jobportal.dto.shared.ApiResponse;
import com.jobportal.entity.Employer;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.EmployerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/employers")
@RequiredArgsConstructor
public class EmployerApiController {

    private final EmployerService employerService;

    @GetMapping("/me")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<EmployerResponse>> getCurrentEmployer() {
        Long empId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(employerService.toResponse(employerService.getEmployerById(empId))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<EmployerResponse>> getEmployer(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(employerService.toResponse(employerService.getEmployerById(id))));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<EmployerResponse>> updateProfile(@Valid @RequestBody EmployerProfileRequest request) {
        Long empId = SecurityUtils.getCurrentUserId();
        Employer updated = employerService.updateProfile(empId, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated", employerService.toResponse(updated)));
    }

    @PostMapping("/locations")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<String>> addLocation(@Valid @RequestBody OfficeLocationRequest request) {
        Long empId = SecurityUtils.getCurrentUserId();
        employerService.addOfficeLocation(empId, request);
        return ResponseEntity.ok(ApiResponse.success("Location added"));
    }

    @DeleteMapping("/locations/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<String>> deleteLocation(@PathVariable Long id) {
        employerService.deleteOfficeLocation(id);
        return ResponseEntity.ok(ApiResponse.success("Location removed"));
    }

    @PostMapping("/team")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<String>> addTeamMember(@Valid @RequestBody TeamMemberRequest request) {
        Long empId = SecurityUtils.getCurrentUserId();
        employerService.addTeamMember(empId, request);
        return ResponseEntity.ok(ApiResponse.success("Team member added"));
    }

    @DeleteMapping("/team/{id}")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<ApiResponse<String>> removeTeamMember(@PathVariable Long id) {
        employerService.removeTeamMember(id);
        return ResponseEntity.ok(ApiResponse.success("Team member removed"));
    }
}
