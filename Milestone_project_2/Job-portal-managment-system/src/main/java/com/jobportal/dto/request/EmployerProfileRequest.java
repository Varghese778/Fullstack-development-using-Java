package com.jobportal.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class EmployerProfileRequest {
    @Size(min = 2, max = 200) private String companyName;
    private String companyWebsite;
    private String phoneNumber;
    private String industry;
    private String companySize;
    private String headquartersLocation;
    @Size(max = 1000) private String description;
    private Integer foundedYear;
    private String culture;
    private String benefits;
    private String contactPerson;
    private String contactEmail;
    private String gstNumber;
}
