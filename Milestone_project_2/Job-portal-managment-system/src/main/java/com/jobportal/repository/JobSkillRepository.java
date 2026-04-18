package com.jobportal.repository;

import com.jobportal.entity.JobSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobSkillRepository extends JpaRepository<JobSkill, Long> {
    List<JobSkill> findByJobJobId(Long jobId);
    void deleteByJobJobId(Long jobId);
}
