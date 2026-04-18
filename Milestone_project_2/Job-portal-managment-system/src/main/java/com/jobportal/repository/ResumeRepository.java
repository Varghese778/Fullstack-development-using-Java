package com.jobportal.repository;

import com.jobportal.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, Long> {
    List<Resume> findByUserUserIdAndIsActiveOrderByUploadedDateDesc(Long userId, Boolean isActive);
    Optional<Resume> findByUserUserIdAndIsPrimary(Long userId, Boolean isPrimary);
    long countByUserUserIdAndIsActive(Long userId, Boolean isActive);
    Optional<Resume> findByChecksumHash(String checksumHash);
}
