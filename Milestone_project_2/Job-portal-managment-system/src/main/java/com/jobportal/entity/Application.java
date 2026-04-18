package com.jobportal.entity;

import com.jobportal.enums.*;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a job application submitted by a user.
 */
@Entity
@Table(name = "applications", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"job_id", "user_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    @ToString.Exclude
    private Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id")
    @ToString.Exclude
    private Resume resume;

    @Column(length = 2000)
    private String coverLetter;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private ApplicationStatusEnum status = ApplicationStatusEnum.SUBMITTED;

    @CreationTimestamp
    private LocalDateTime applicationDate;

    @UpdateTimestamp
    private LocalDateTime lastUpdatedDate;

    private LocalDateTime statusUpdateDate;

    private Integer rating; // 1-5

    @Builder.Default
    private Boolean isStarred = false;

    @Column(length = 500)
    private String tags; // JSON array

    @Column(length = 1000)
    private String notes;

    @Enumerated(EnumType.STRING)
    private RejectionReasonEnum rejectionReason;

    private LocalDateTime rejectionDate;

    private LocalDateTime withdrawnDate;

    @Column(length = 500)
    private String withdrawalReason;

    private LocalDateTime interviewScheduledDate;

    @Enumerated(EnumType.STRING)
    private InterviewTypeEnum interviewType;

    @OneToMany(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<ApplicationHistory> statusHistory = new ArrayList<>();

    @OneToOne(mappedBy = "application", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    private InterviewSchedule interviewSchedule;
}
