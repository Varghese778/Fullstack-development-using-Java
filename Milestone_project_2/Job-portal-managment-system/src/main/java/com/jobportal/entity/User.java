package com.jobportal.entity;

import com.jobportal.enums.AvailabilityStatusEnum;
import com.jobportal.enums.RoleEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a job seeker (student) user in the system.
 */
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotNull
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String firstName;

    @NotNull
    @Size(min = 2, max = 100)
    @Column(nullable = false)
    private String lastName;

    private String phoneNumber;

    private LocalDate dateOfBirth;

    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RoleEnum role = RoleEnum.STUDENT;

    @Column(length = 500)
    private String bio;

    private String profilePictureUrl;

    private String portfolioUrl;

    private String linkedinUrl;

    private String githubUrl;

    private Double expectedSalary;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private AvailabilityStatusEnum availabilityStatus = AvailabilityStatusEnum.AVAILABLE;

    @Builder.Default
    private Integer profileCompleteness = 0;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Boolean isEmailVerified = false;

    private String emailVerificationToken;

    private String passwordResetToken;

    private LocalDateTime passwordResetTokenExpiry;

    @Builder.Default
    private Integer failedLoginAttempts = 0;

    @Builder.Default
    private Boolean isAccountLocked = false;

    private LocalDateTime accountLockedUntil;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime lastLoginAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<UserEducation> educations = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<UserExperience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<UserSkill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Resume> resumes = new ArrayList<>();

    /**
     * Returns the user's full name.
     */
    public String getFullName() {
        return firstName + " " + lastName;
    }
}
