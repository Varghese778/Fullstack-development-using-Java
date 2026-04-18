package com.jobportal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PasswordResetRequest {
    private String email;
    private String token;
    @NotBlank @Size(min = 8, message = "Password must be at least 8 characters")
    private String newPassword;
    @NotBlank
    private String confirmPassword;
}
