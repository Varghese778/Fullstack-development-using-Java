package com.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * Represents a location where a job is offered.
 */
@Entity
@Table(name = "job_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JobLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    @ToString.Exclude
    private Job job;

    private String city;
    private String state;
    @Builder.Default
    private String country = "India";
    private String pincode;
}
