package com.climeet.climeet_backend.domain.climbingrecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClimbingRecordRepository extends JpaRepository<ClimbingRecord, Long> {

    List<ClimbingRecord> findByClimbingDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<ClimbingRecord> findById(Long id);
}