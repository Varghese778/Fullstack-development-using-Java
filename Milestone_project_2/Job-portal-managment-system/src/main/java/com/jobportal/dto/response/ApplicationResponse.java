package com.jobportal.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ApplicationResponse {
    private Long applicationId;
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String companyLogo;
    private Long userId;
    private String candidateName;
    private String candidateEmail;
    private String status;
    private String coverLetter;
    private Integer rating;
    private Boolean isStarred;
    private Long resumeId;
    private String resumeName;
    private LocalDateTime applicationDate;
    private LocalDateTime statusUpdateDate;
    private String rejectionReason;
    private String interviewType;
    private LocalDateTime interviewScheduledDate;
}
