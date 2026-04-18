package com.jobportal.service;

import com.jobportal.dto.request.RegisterRequest;
import com.jobportal.dto.request.PasswordResetRequest;
import com.jobportal.entity.*;
import com.jobportal.enums.*;
import com.jobportal.exception.*;
import com.jobportal.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Handles registration, password reset, email verification, and login tracking.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;
    private final PasswordEncoder passwordEncoder;
    private final LoginAttemptRepository loginAttemptRepository;
    private final PasswordHistoryRepository passwordHistoryRepository;
    private final AuditLogService auditLogService;

    /**
     * Register a new student user.
     */
    public User registerStudent(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("Passwords do not match");
        }
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phoneNumber(request.getPhoneNumber())
                .role(RoleEnum.STUDENT)
                .emailVerificationToken(UUID.randomUUID().toString())
                .build();

        User saved = userRepository.save(user);
        savePasswordHistory(saved.getUserId(), saved.getPassword());
        auditLogService.log(saved.getUserId(), AuditActionEnum.ACCOUNT_CREATED, "User", saved.getUserId(), "SUCCESS", null);
        log.info("Student registered: {}", saved.getEmail());
        return saved;
    }

    /**
     * Register a new employer.
     */
    public Employer registerEmployer(RegisterRequest request) {
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("Passwords do not match");
        }
        if (employerRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already registered");
        }

        Employer employer = Employer.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .companyName(request.getCompanyName())
                .phoneNumber(request.getPhoneNumber())
                .emailVerificationToken(UUID.randomUUID().toString())
                .build();

        if (request.getIndustry() != null) {
            try { employer.setIndustry(IndustryEnum.valueOf(request.getIndustry())); } catch (Exception ignored) {}
        }
        if (request.getCompanySize() != null) {
            try { employer.setCompanySize(CompanySizeEnum.valueOf(request.getCompanySize())); } catch (Exception ignored) {}
        }

        Employer saved = employerRepository.save(employer);
        auditLogService.log(saved.getEmployerId(), AuditActionEnum.ACCOUNT_CREATED, "Employer", saved.getEmployerId(), "SUCCESS", null);
        log.info("Employer registered: {}", saved.getEmail());
        return saved;
    }

    /**
     * Initiate password reset by generating a token.
     */
    public String initiatePasswordReset(String email) {
        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusHours(1);

        userRepository.findByEmail(email).ifPresentOrElse(user -> {
            user.setPasswordResetToken(token);
            user.setPasswordResetTokenExpiry(expiry);
            userRepository.save(user);
        }, () -> {
            employerRepository.findByEmail(email).ifPresentOrElse(employer -> {
                employer.setPasswordResetToken(token);
                employer.setPasswordResetTokenExpiry(expiry);
                employerRepository.save(employer);
            }, () -> {
                throw new ResourceNotFoundException("No account found with email: " + email);
            });
        });

        log.info("Password reset initiated for: {}", email);
        return token;
    }

    /**
     * Reset password using a valid token.
     */
    public void resetPassword(PasswordResetRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmPassword())) {
            throw new ValidationException("Passwords do not match");
        }

        // Try User table
        var userOpt = userRepository.findByPasswordResetToken(request.getToken());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new ValidationException("Password reset token has expired");
            }
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            user.setPasswordResetToken(null);
            user.setPasswordResetTokenExpiry(null);
            userRepository.save(user);
            savePasswordHistory(user.getUserId(), user.getPassword());
            auditLogService.log(user.getUserId(), AuditActionEnum.PASSWORD_RESET, "User", user.getUserId(), "SUCCESS", null);
            return;
        }

        // Try Employer table
        var empOpt = employerRepository.findByPasswordResetToken(request.getToken());
        if (empOpt.isPresent()) {
            Employer emp = empOpt.get();
            if (emp.getPasswordResetTokenExpiry().isBefore(LocalDateTime.now())) {
                throw new ValidationException("Password reset token has expired");
            }
            emp.setPassword(passwordEncoder.encode(request.getNewPassword()));
            emp.setPasswordResetToken(null);
            emp.setPasswordResetTokenExpiry(null);
            employerRepository.save(emp);
            auditLogService.log(emp.getEmployerId(), AuditActionEnum.PASSWORD_RESET, "Employer", emp.getEmployerId(), "SUCCESS", null);
            return;
        }

        throw new ResourceNotFoundException("Invalid password reset token");
    }

    /**
     * Record a login attempt.
     */
    public void recordLoginAttempt(String email, boolean success, String ipAddress, String userAgent) {
        LoginAttempt attempt = LoginAttempt.builder()
                .email(email)
                .isSuccess(success)
                .ipAddress(ipAddress)
                .userAgent(userAgent)
                .attemptTime(LocalDateTime.now())
                .failureReason(success ? null : "Invalid credentials")
                .build();
        loginAttemptRepository.save(attempt);

        if (success) {
            // update lastLoginAt
            userRepository.findByEmail(email).ifPresent(u -> {
                u.setLastLoginAt(LocalDateTime.now());
                u.setFailedLoginAttempts(0);
                userRepository.save(u);
            });
            employerRepository.findByEmail(email).ifPresent(e -> {
                e.setLastLoginAt(LocalDateTime.now());
                e.setFailedLoginAttempts(0);
                employerRepository.save(e);
            });
        } else {
            // Increment failed attempts, lock after 5
            userRepository.findByEmail(email).ifPresent(u -> {
                u.setFailedLoginAttempts(u.getFailedLoginAttempts() + 1);
                if (u.getFailedLoginAttempts() >= 5) {
                    u.setIsAccountLocked(true);
                    u.setAccountLockedUntil(LocalDateTime.now().plusMinutes(30));
                    auditLogService.log(u.getUserId(), AuditActionEnum.ACCOUNT_LOCKED, "User", u.getUserId(), "SUCCESS", "Too many failed attempts");
                }
                userRepository.save(u);
            });
            employerRepository.findByEmail(email).ifPresent(e -> {
                e.setFailedLoginAttempts(e.getFailedLoginAttempts() + 1);
                if (e.getFailedLoginAttempts() >= 5) {
                    e.setIsAccountLocked(true);
                    e.setAccountLockedUntil(LocalDateTime.now().plusMinutes(30));
                }
                employerRepository.save(e);
            });
        }
    }

    private void savePasswordHistory(Long userId, String passwordHash) {
        passwordHistoryRepository.save(PasswordHistory.builder()
                .userId(userId).passwordHash(passwordHash).changedAt(LocalDateTime.now()).build());
    }
}
