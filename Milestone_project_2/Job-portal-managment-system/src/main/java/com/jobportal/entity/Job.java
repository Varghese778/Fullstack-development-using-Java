package com.jobportal.entity;

import com.jobportal.enums.*;
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
 * Represents a job posting created by an employer.
 */
@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    @ToString.Exclude
    private Employer employer;

    @NotNull
    @Size(min = 3, max = 100)
    @Column(nullable = false)
    private String jobTitle;

    @NotNull
    @Column(nullable = false, length = 5000)
    private String jobDescription;

    private String department;

    @Enumerated(EnumType.STRING)
    private IndustryEnum category;

    private Double salaryMin;

    private Double salaryMax;

    @Builder.Default
    private String currency = "INR";

    @Enumerated(EnumType.STRING)
    private ExperienceLevelEnum experienceLevel;

    @Enumerated(EnumType.STRING)
    private EmploymentTypeEnum employmentType;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private RemotePolicyEnum remotePolicy = RemotePolicyEnum.ON_SITE;

    private String companyName;

    private String companyLogo;

    @Builder.Default
    private Integer numberOfPositions = 1;

    private LocalDate applicationDeadline;

    @Column(length = 2000)
    private String benefits; // JSON array

    @Column(length = 2000)
    private String skillsRequired; // JSON array of skill names

    private String reportingManager;

    private String externalJobUrl;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private JobStatusEnum status = JobStatusEnum.DRAFT;

    @Builder.Default
    private Boolean isPublished = false;

    private LocalDateTime publishedDate;

    private LocalDateTime closedDate;

    @Builder.Default
    private Integer viewCount = 0;

    @Builder.Default
    private Integer applicationCount = 0;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private Long createdBy;

    private Long updatedBy;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<JobLocation> locations = new ArrayList<>();

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<JobSkill> requiredSkills = new ArrayList<>();

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<Application> applications = new ArrayList<>();
}
