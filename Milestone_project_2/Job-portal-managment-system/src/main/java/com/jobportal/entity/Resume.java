package com.jobportal.entity;

import com.jobportal.enums.StorageTypeEnum;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents an uploaded resume file.
 */
@Entity
@Table(name = "resumes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Resume {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long resumeId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    @ToString.Exclude
    private User user;

    private String resumeName;

    private String originalFileName;

    private String fileExtension;

    private Long fileSize; // bytes

    private String filePath;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private StorageTypeEnum storageType = StorageTypeEnum.LOCAL;

    private String mimeType;

    @Builder.Default
    private Boolean isPrimary = false;

    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime uploadedDate;

    @UpdateTimestamp
    private LocalDateTime lastModifiedDate;

    @Builder.Default
    private Integer downloadCount = 0;

    @Builder.Default
    private Integer applicationCount = 0;

    private String checksumHash;
}
