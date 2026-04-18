package com.jobportal.entity;

import com.jobportal.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an employer / company in the system.
 */
@Entity
@Table(name = "employers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Employer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long employerId;

    @NotNull
    @Email
    @Column(unique = true, nullable = false)
    private String email;

    @NotNull
    @Column(nullable = false)
    private String password;

    @NotNull
    @Size(min = 2, max = 200)
    @Column(nullable = false)
    private String companyName;

    private String companyWebsite;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private IndustryEnum industry;

    @Enumerated(EnumType.STRING)
    private CompanySizeEnum companySize;

    private String headquartersLocation;

    @Column(length = 1000)
    private String description;

    private String logoUrl;

    @Builder.Default
    private Boolean isVerified = false;

    private LocalDateTime verificationDate;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ApprovalStatusEnum approvalStatus = ApprovalStatusEnum.PENDING;

    private Long approvedBy;

    private String contactPerson;

    private String contactEmail;

    private String gstNumber;

    private Integer foundedYear;

    private String culture;

    private String benefits;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private RoleEnum role = RoleEnum.EMPLOYER;

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

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<OfficeLocation> officeLocations = new ArrayList<>();

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<EmployerSocialLink> socialLinks = new ArrayList<>();

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<TeamMember> teamMembers = new ArrayList<>();

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Job> jobs = new ArrayList<>();
}
