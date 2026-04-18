package com.jobportal.repository;

import com.jobportal.entity.ResumeDownloadLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResumeDownloadLogRepository extends JpaRepository<ResumeDownloadLog, Long> {
    List<ResumeDownloadLog> findByResumeResumeIdOrderByDownloadedAtDesc(Long resumeId);
    long countByResumeResumeId(Long resumeId);
}
