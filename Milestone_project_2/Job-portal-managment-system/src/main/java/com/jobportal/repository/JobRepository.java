package com.jobportal.repository;

import com.jobportal.entity.Job;
import com.jobportal.enums.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

    Page<Job> findByStatus(JobStatusEnum status, Pageable pageable);

    Page<Job> findByEmployerEmployerId(Long employerId, Pageable pageable);

    Page<Job> findByEmployerEmployerIdAndStatus(Long employerId, JobStatusEnum status, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND (LOWER(j.jobTitle) LIKE LOWER(CONCAT('%',:keyword,'%')) OR LOWER(j.jobDescription) LIKE LOWER(CONCAT('%',:keyword,'%')))")
    Page<Job> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' ORDER BY j.viewCount DESC")
    List<Job> findTrendingJobs(Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND j.applicationDeadline < :today")
    List<Job> findExpiredJobs(@Param("today") LocalDate today);

    Page<Job> findByStatusAndCategory(JobStatusEnum status, IndustryEnum category, Pageable pageable);

    Page<Job> findByStatusAndEmploymentType(JobStatusEnum status, EmploymentTypeEnum type, Pageable pageable);

    Page<Job> findByStatusAndExperienceLevel(JobStatusEnum status, ExperienceLevelEnum level, Pageable pageable);

    Page<Job> findByStatusAndRemotePolicy(JobStatusEnum status, RemotePolicyEnum policy, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND j.salaryMin >= :minSalary AND j.salaryMax <= :maxSalary")
    Page<Job> findBySalaryRange(@Param("minSalary") Double minSalary, @Param("maxSalary") Double maxSalary, Pageable pageable);

    long countByStatus(JobStatusEnum status);

    long countByEmployerEmployerId(Long employerId);

    long countByEmployerEmployerIdAndStatus(Long employerId, JobStatusEnum status);

    @Query("SELECT COUNT(j) FROM Job j WHERE j.createdAt >= :since")
    long countNewJobsLast30Days(@Param("since") java.time.LocalDateTime since);
}
