package com.jobportal.dto.request;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class OfficeLocationRequest {
    private String locationName;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private String country;
    private Boolean isHeadquarters;
}
