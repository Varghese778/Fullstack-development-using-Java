package com.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a saved or custom report definition.
 */
@Entity
@Table(name = "reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reportId;

    private String reportName;

    private String reportType; // PREDEFINED, CUSTOM

    private Long creatorId;

    @Column(length = 500)
    private String description;

    @Column(length = 5000)
    private String queryDefinition; // JSON

    @Column(length = 2000)
    private String dataSegments; // JSON array

    @Column(length = 2000)
    private String chartConfigs; // JSON array

    @Column(length = 2000)
    private String metrics; // JSON array

    private String dateRange;

    @Column(length = 1000)
    private String schedule; // JSON for recurring

    private LocalDateTime lastGeneratedAt;

    private LocalDateTime nextScheduleTime;

    @Builder.Default
    private Boolean isActive = true;

    @Builder.Default
    private Boolean isPublic = false;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
