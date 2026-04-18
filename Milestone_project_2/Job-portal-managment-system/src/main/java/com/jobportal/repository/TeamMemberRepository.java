package com.jobportal.repository;

import com.jobportal.entity.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TeamMemberRepository extends JpaRepository<TeamMember, Long> {
    List<TeamMember> findByEmployerEmployerIdAndIsActive(Long employerId, Boolean isActive);
    List<TeamMember> findByEmployerEmployerId(Long employerId);
}
