package com.jobportal.dto.request;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ApplicationRequest {
    private Long resumeId;
    private String coverLetter;
}
