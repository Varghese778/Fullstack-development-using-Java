package com.jobportal.repository;

import com.jobportal.entity.UserSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface UserSkillRepository extends JpaRepository<UserSkill, Long> {
    List<UserSkill> findByUserUserIdOrderByEndorsementCountDesc(Long userId);
    List<UserSkill> findByUserUserId(Long userId);
}
