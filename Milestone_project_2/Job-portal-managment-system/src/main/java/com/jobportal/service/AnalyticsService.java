package com.jobportal.service;

import com.jobportal.dto.response.DashboardStatsResponse;
import com.jobportal.enums.*;
import com.jobportal.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class AnalyticsService {

    private final UserRepository userRepository;
    private final EmployerRepository employerRepository;
    private final JobRepository jobRepository;
    private final ApplicationRepository applicationRepository;

    public DashboardStatsResponse getStudentDashboard(Long userId) {
        return DashboardStatsResponse.builder()
                .totalApplications(applicationRepository.countByUserUserId(userId))
                .shortlisted(applicationRepository.countByUserUserIdAndStatus(userId, ApplicationStatusEnum.SHORTLISTED))
                .underReview(applicationRepository.countByUserUserIdAndStatus(userId, ApplicationStatusEnum.UNDER_REVIEW))
                .interviewsScheduled(applicationRepository.countByUserUserIdAndStatus(userId, ApplicationStatusEnum.INTERVIEWED))
                .rejected(applicationRepository.countByUserUserIdAndStatus(userId, ApplicationStatusEnum.REJECTED))
                .hired(applicationRepository.countByUserUserIdAndStatus(userId, ApplicationStatusEnum.HIRED))
                .build();
    }

    public DashboardStatsResponse getEmployerDashboard(Long employerId) {
        return DashboardStatsResponse.builder()
                .totalJobs(jobRepository.countByEmployerEmployerId(employerId))
                .activeJobs(jobRepository.countByEmployerEmployerIdAndStatus(employerId, JobStatusEnum.ACTIVE))
                .totalApplicationsReceived(applicationRepository.countByEmployerId(employerId))
                .shortlistedCandidates(applicationRepository.countByEmployerIdAndStatus(employerId, ApplicationStatusEnum.SHORTLISTED))
                .hiredCandidates(applicationRepository.countByEmployerIdAndStatus(employerId, ApplicationStatusEnum.HIRED))
                .build();
    }

    public DashboardStatsResponse getAdminDashboard() {
        java.time.LocalDateTime thirtyDaysAgo = java.time.LocalDateTime.now().minusDays(30);
        return DashboardStatsResponse.builder()
                .totalUsers(userRepository.countByRole(RoleEnum.STUDENT))
                .totalEmployers(employerRepository.count())
                .newUsersThisMonth(userRepository.countNewUsersLast30Days(RoleEnum.STUDENT, thirtyDaysAgo))
                .totalJobsPosted(jobRepository.countByStatus(JobStatusEnum.ACTIVE))
                .totalApplicationsSystem(applicationRepository.count())
                .totalHires(applicationRepository.count()) // simplified
                .build();
    }
}
