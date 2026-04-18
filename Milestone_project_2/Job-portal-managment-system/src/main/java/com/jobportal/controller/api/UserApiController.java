package com.jobportal.controller.api;

import com.jobportal.dto.request.*;
import com.jobportal.dto.response.UserResponse;
import com.jobportal.dto.shared.ApiResponse;
import com.jobportal.entity.User;
import com.jobportal.security.SecurityUtils;
import com.jobportal.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserApiController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserResponse>> getCurrentUser() {
        Long userId = SecurityUtils.getCurrentUserId();
        return ResponseEntity.ok(ApiResponse.success(userService.toResponse(userService.getUserById(userId))));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUser(@PathVariable Long id) {
        return ResponseEntity.ok(ApiResponse.success(userService.toResponse(userService.getUserById(id))));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserResponse>> updateProfile(@Valid @RequestBody UserProfileRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        User updated = userService.updateProfile(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Profile updated", userService.toResponse(updated)));
    }

    @PostMapping("/education")
    public ResponseEntity<ApiResponse<String>> addEducation(@Valid @RequestBody EducationRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.addEducation(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Education added"));
    }

    @DeleteMapping("/education/{id}")
    public ResponseEntity<ApiResponse<String>> deleteEducation(@PathVariable Long id) {
        userService.deleteEducation(id);
        return ResponseEntity.ok(ApiResponse.success("Education removed"));
    }

    @PostMapping("/experience")
    public ResponseEntity<ApiResponse<String>> addExperience(@Valid @RequestBody ExperienceRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.addExperience(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Experience added"));
    }

    @DeleteMapping("/experience/{id}")
    public ResponseEntity<ApiResponse<String>> deleteExperience(@PathVariable Long id) {
        userService.deleteExperience(id);
        return ResponseEntity.ok(ApiResponse.success("Experience removed"));
    }

    @PostMapping("/skills")
    public ResponseEntity<ApiResponse<String>> addSkill(@Valid @RequestBody SkillRequest request) {
        Long userId = SecurityUtils.getCurrentUserId();
        userService.addSkill(userId, request);
        return ResponseEntity.ok(ApiResponse.success("Skill added"));
    }

    @DeleteMapping("/skills/{id}")
    public ResponseEntity<ApiResponse<String>> deleteSkill(@PathVariable Long id) {
        userService.deleteSkill(id);
        return ResponseEntity.ok(ApiResponse.success("Skill removed"));
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "20") int size) {
        Page<UserResponse> users = userService.getAllUsers(PageRequest.of(page, size)).map(userService::toResponse);
        return ResponseEntity.ok(ApiResponse.success(users));
    }
}
