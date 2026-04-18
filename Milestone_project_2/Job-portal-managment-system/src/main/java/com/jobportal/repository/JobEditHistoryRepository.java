package com.jobportal.repository;

import com.jobportal.entity.JobEditHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobEditHistoryRepository extends JpaRepository<JobEditHistory, Long> {
    List<JobEditHistory> findByJobJobIdOrderByEditedAtDesc(Long jobId);
}
