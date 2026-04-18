package com.jobportal.repository;

import com.jobportal.entity.JobViewHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface JobViewHistoryRepository extends JpaRepository<JobViewHistory, Long> {
    Optional<JobViewHistory> findByJobJobIdAndUserIdAndViewedAtAfter(Long jobId, Long userId, LocalDateTime after);
    long countByJobJobId(Long jobId);
}
