package com.jobportal.repository;

import com.jobportal.entity.User;
import com.jobportal.enums.RoleEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByEmailAndRole(String email, RoleEnum role);

    boolean existsByEmail(String email);

    Optional<User> findByEmailVerificationToken(String token);

    Optional<User> findByPasswordResetToken(String token);

    Page<User> findByRole(RoleEnum role, Pageable pageable);

    Page<User> findByIsActive(Boolean isActive, Pageable pageable);

    @Query("SELECT u FROM User u WHERE u.role = :role AND (LOWER(u.firstName) LIKE LOWER(CONCAT('%',:keyword,'%')) OR LOWER(u.lastName) LIKE LOWER(CONCAT('%',:keyword,'%')) OR LOWER(u.email) LIKE LOWER(CONCAT('%',:keyword,'%')))")
    Page<User> searchUsers(@Param("role") RoleEnum role, @Param("keyword") String keyword, Pageable pageable);

    long countByRole(RoleEnum role);

    long countByIsActive(Boolean isActive);

    @Query("SELECT COUNT(u) FROM User u WHERE u.role = :role AND u.createdAt >= :since")
    long countNewUsersLast30Days(@Param("role") RoleEnum role, @Param("since") java.time.LocalDateTime since);
}
