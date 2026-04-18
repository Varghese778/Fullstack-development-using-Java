package com.jobportal.controller.api;

import com.jobportal.dto.request.LoginRequest;
import com.jobportal.dto.request.RegisterRequest;
import com.jobportal.dto.shared.ApiResponse;
import com.jobportal.enums.RoleEnum;
import com.jobportal.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthApiController {

    private final AuthService authService;

    @PostMapping("/register/student")
    public ResponseEntity<ApiResponse<String>> registerStudent(@Valid @RequestBody RegisterRequest request) {
        request.setRole(RoleEnum.STUDENT);
        authService.registerStudent(request);
        return ResponseEntity.ok(ApiResponse.success("Student registered successfully"));
    }

    @PostMapping("/register/employer")
    public ResponseEntity<ApiResponse<String>> registerEmployer(@Valid @RequestBody RegisterRequest request) {
        request.setRole(RoleEnum.EMPLOYER);
        authService.registerEmployer(request);
        return ResponseEntity.ok(ApiResponse.success("Employer registered successfully"));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<String>> forgotPassword(@RequestParam String email) {
        authService.initiatePasswordReset(email);
        return ResponseEntity.ok(ApiResponse.success("Password reset email sent"));
    }
}
