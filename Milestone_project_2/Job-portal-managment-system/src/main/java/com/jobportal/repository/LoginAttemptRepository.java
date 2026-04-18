package com.jobportal.repository;

import com.jobportal.entity.LoginAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoginAttemptRepository extends JpaRepository<LoginAttempt, Long> {
    List<LoginAttempt> findByEmailOrderByAttemptTimeDesc(String email);
    long countByEmailAndIsSuccessAndAttemptTimeAfter(String email, Boolean isSuccess, LocalDateTime after);
}
