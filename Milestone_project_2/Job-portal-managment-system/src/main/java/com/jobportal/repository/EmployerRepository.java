package com.jobportal.repository;

import com.jobportal.entity.Employer;
import com.jobportal.enums.ApprovalStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerRepository extends JpaRepository<Employer, Long> {

    Optional<Employer> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Employer> findByEmailVerificationToken(String token);

    Optional<Employer> findByPasswordResetToken(String token);

    Page<Employer> findByApprovalStatus(ApprovalStatusEnum status, Pageable pageable);

    Page<Employer> findByIsActive(Boolean isActive, Pageable pageable);

    @Query("SELECT e FROM Employer e WHERE LOWER(e.companyName) LIKE LOWER(CONCAT('%',:keyword,'%')) OR LOWER(e.email) LIKE LOWER(CONCAT('%',:keyword,'%'))")
    Page<Employer> searchEmployers(@Param("keyword") String keyword, Pageable pageable);

    long countByApprovalStatus(ApprovalStatusEnum status);

    long countByIsVerified(Boolean isVerified);
}
