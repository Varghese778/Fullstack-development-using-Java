package com.jobportal.repository;

import com.jobportal.entity.Analytics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface AnalyticsRepository extends JpaRepository<Analytics, Long> {
    List<Analytics> findByAnalyticsTypeAndDateBetweenOrderByDateAsc(String type, LocalDate start, LocalDate end);
    List<Analytics> findByEmployerIdAndDateBetween(Long employerId, LocalDate start, LocalDate end);
}
