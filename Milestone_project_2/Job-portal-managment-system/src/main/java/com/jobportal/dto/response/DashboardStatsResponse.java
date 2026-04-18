package com.jobportal.dto.response;

import lombok.*;

@Data @NoArgsConstructor @AllArgsConstructor @Builder
public class DashboardStatsResponse {
    // Student stats
    private Integer profileCompletion;
    private Long totalApplications;
    private Long shortlisted;
    private Long underReview;
    private Long interviewsScheduled;
    private Long rejected;
    private Long hired;

    // Employer stats
    private Long totalJobs;
    private Long activeJobs;
    private Long totalApplicationsReceived;
    private Long shortlistedCandidates;
    private Long hiredCandidates;
    private Long applicationsLast7Days;

    // Admin stats
    private Long totalUsers;
    private Long totalEmployers;
    private Long newUsersThisMonth;
    private Long totalJobsPosted;
    private Long totalApplicationsSystem;
    private Long totalHires;
}
