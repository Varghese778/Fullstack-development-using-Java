package com.jobportal.repository;

import com.jobportal.entity.InterviewSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InterviewScheduleRepository extends JpaRepository<InterviewSchedule, Long> {

    Optional<InterviewSchedule> findByApplicationApplicationId(Long applicationId);

    @Query("SELECT i FROM InterviewSchedule i WHERE i.interviewDate = :date AND i.reminderSent = false")
    List<InterviewSchedule> findUpcomingInterviewsForReminder(@Param("date") LocalDate date);

    @Query("SELECT i FROM InterviewSchedule i WHERE i.application.user.userId = :userId AND i.interviewDate >= CURRENT_DATE ORDER BY i.interviewDate ASC")
    List<InterviewSchedule> findUpcomingByUserId(@Param("userId") Long userId);

    @Query("SELECT i FROM InterviewSchedule i WHERE i.application.job.employer.employerId = :employerId AND i.interviewDate >= CURRENT_DATE ORDER BY i.interviewDate ASC")
    List<InterviewSchedule> findUpcomingByEmployerId(@Param("employerId") Long employerId);
}
