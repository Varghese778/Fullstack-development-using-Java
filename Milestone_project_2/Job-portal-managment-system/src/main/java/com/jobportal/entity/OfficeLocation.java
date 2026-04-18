package com.jobportal.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * Represents an office location of an employer.
 */
@Entity
@Table(name = "office_locations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfficeLocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long locationId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employer_id", nullable = false)
    @ToString.Exclude
    private Employer employer;

    private String locationName;

    private String address;

    private String city;

    private String state;

    private String pincode;

    @Builder.Default
    private String country = "India";

    private Double latitude;

    private Double longitude;

    @Builder.Default
    private Boolean isHeadquarters = false;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
