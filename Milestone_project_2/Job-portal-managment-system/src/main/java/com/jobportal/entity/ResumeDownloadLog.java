package com.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Audit trail for resume downloads.
 */
@Entity
@Table(name = "resume_download_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResumeDownloadLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    @ToString.Exclude
    private Resume resume;

    private Long downloadedBy;

    private LocalDateTime downloadedAt;

    private String ipAddress;

    private String userAgent;
}
