package com.jobportal.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class JobResponse {
    private Long jobId;
    private String jobTitle;
    private String companyName;
    private String companyLogo;
    private String department;
    private String category;
    private Double salaryMin;
    private Double salaryMax;
    private String experienceLevel;
    private String employmentType;
    private String remotePolicy;
    private String status;
    private Integer viewCount;
    private Integer applicationCount;
    private Integer numberOfPositions;
    private String applicationDeadline;
    private List<String> locations;
    private List<String> skills;
    private LocalDateTime publishedDate;
    private LocalDateTime createdAt;
    private Long employerId;
}
