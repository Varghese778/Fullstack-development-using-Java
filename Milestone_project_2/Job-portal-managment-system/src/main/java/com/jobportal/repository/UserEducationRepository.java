package com.jobportal.repository;

import com.jobportal.entity.UserEducation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserEducationRepository extends JpaRepository<UserEducation, Long> {
    List<UserEducation> findByUserUserIdOrderByGraduationYearDesc(Long userId);
}
