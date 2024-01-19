package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordStatisticsInfo;
import jakarta.persistence.Tuple;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClimbingRecordRepository extends JpaRepository<ClimbingRecord, Long> {

    List<ClimbingRecord> findByClimbingDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<ClimbingRecord> findById(Long id);

    @Query("SELECT " +
        "   SUM(HOUR(cr.climbingTime) * 3600 + MINUTE(cr.climbingTime) * 60 + SECOND(cr.climbingTime)) as totalTime, " +
        "   SUM(cr.totalCompletedCount) as totalCompletedCount, " +
        "   SUM(cr.attemptRouteCount) as attemptRouteCount, " +
        "   SUM(cr.avgDifficulty) as avgDifficulty " +
        "FROM ClimbingRecord cr " +
        "WHERE cr.climbingDate BETWEEN :startDate AND :endDate")
    Tuple getStatisticsInfoBetween(@Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

}