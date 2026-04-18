package com.jobportal.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class ResumeResponse {
    private Long resumeId;
    private String resumeName;
    private String originalFileName;
    private String fileExtension;
    private Long fileSize;
    private Boolean isPrimary;
    private Integer downloadCount;
    private Integer applicationCount;
    private LocalDateTime uploadedDate;
}
