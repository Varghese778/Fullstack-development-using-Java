package com.jobportal.repository;

import com.jobportal.entity.JobLocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface JobLocationRepository extends JpaRepository<JobLocation, Long> {
    List<JobLocation> findByJobJobId(Long jobId);
    void deleteByJobJobId(Long jobId);
}
