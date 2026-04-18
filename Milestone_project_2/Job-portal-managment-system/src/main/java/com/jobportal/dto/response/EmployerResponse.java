package com.jobportal.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployerResponse {
    private Long employerId;
    private String email;
    private String companyName;
    private String companyWebsite;
    private String phoneNumber;
    private String industry;
    private String companySize;
    private String headquartersLocation;
    private String description;
    private String logoUrl;
    private Boolean isVerified;
    private String approvalStatus;
    private String contactPerson;
    private String contactEmail;
    private Integer foundedYear;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
