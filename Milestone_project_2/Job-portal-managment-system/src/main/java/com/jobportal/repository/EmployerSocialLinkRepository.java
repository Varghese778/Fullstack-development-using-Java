package com.jobportal.repository;

import com.jobportal.entity.EmployerSocialLink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EmployerSocialLinkRepository extends JpaRepository<EmployerSocialLink, Long> {
    List<EmployerSocialLink> findByEmployerEmployerId(Long employerId);
}
