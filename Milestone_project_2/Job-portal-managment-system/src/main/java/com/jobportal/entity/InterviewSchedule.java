package com.jobportal.entity;

import com.jobportal.enums.InterviewTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Represents a scheduled interview for an application.
 */
@Entity
@Table(name = "interview_schedules")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InterviewSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long interviewId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "application_id", nullable = false)
    @ToString.Exclude
    private Application application;

    private LocalDate interviewDate;

    private LocalTime interviewTime;

    @Enumerated(EnumType.STRING)
    private InterviewTypeEnum interviewType;

    private String interviewLocation;

    private String videoCallLink;

    @Column(length = 1000)
    private String panelMembers; // JSON array of emails

    @Column(length = 1000)
    private String notes;

    @Builder.Default
    private Boolean reminderSent = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    private Long createdBy;
}
