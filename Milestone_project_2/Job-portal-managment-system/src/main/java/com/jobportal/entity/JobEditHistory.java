package com.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

/**
 * Audit trail for job edits.
 */
@Entity
@Table(name = "job_edit_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobEditHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long historyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    @ToString.Exclude
    private Job job;

    private Long editedBy;
    private String fieldName;

    @Column(length = 2000)
    private String oldValue;

    @Column(length = 2000)
    private String newValue;

    private LocalDateTime editedAt;
    private String changeReason;
}
