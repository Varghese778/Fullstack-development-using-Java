package com.jobportal.repository;

import com.jobportal.entity.Application;
import com.jobportal.enums.ApplicationStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {

    Page<Application> findByUserUserId(Long userId, Pageable pageable);

    Page<Application> findByUserUserIdAndStatus(Long userId, ApplicationStatusEnum status, Pageable pageable);

    Page<Application> findByJobJobId(Long jobId, Pageable pageable);

    Page<Application> findByJobJobIdAndStatus(Long jobId, ApplicationStatusEnum status, Pageable pageable);

    Optional<Application> findByJobJobIdAndUserUserId(Long jobId, Long userId);

    boolean existsByJobJobIdAndUserUserIdAndStatusNot(Long jobId, Long userId, ApplicationStatusEnum status);

    long countByJobJobId(Long jobId);

    long countByJobJobIdAndStatus(Long jobId, ApplicationStatusEnum status);

    long countByUserUserId(Long userId);

    long countByUserUserIdAndStatus(Long userId, ApplicationStatusEnum status);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.employer.employerId = :employerId")
    long countByEmployerId(@Param("employerId") Long employerId);

    @Query("SELECT COUNT(a) FROM Application a WHERE a.job.employer.employerId = :employerId AND a.status = :status")
    long countByEmployerIdAndStatus(@Param("employerId") Long employerId, @Param("status") ApplicationStatusEnum status);

    @Query("SELECT a FROM Application a WHERE a.job.employer.employerId = :employerId")
    Page<Application> findByEmployerId(@Param("employerId") Long employerId, Pageable pageable);

    @Query("SELECT a FROM Application a WHERE a.job.employer.employerId = :employerId AND a.status = :status")
    Page<Application> findByEmployerIdAndStatus(@Param("employerId") Long employerId, @Param("status") ApplicationStatusEnum status, Pageable pageable);
}
