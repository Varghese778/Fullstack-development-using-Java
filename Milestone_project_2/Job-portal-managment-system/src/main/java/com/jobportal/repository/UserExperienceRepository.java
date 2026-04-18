package com.jobportal.repository;

import com.jobportal.entity.UserExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserExperienceRepository extends JpaRepository<UserExperience, Long> {
    List<UserExperience> findByUserUserIdOrderByStartDateDesc(Long userId);
}
