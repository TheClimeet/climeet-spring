package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordStatisticsInfo;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClimbingRecordRepository extends JpaRepository<ClimbingRecord, Long> {

    List<ClimbingRecord> findByClimbingDateBetween(LocalDate startDate, LocalDate endDate);

    Optional<ClimbingRecord> findById(Long id);

    @Query(value = "SELECT " +
        "NEW com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordStatisticsInfo("
        +
        "   SUM(HOUR(cr.climbingTime) * 3600 + MINUTE(cr.climbingTime) * 60 + SECOND(cr.climbingTime)), " +
        "   SUM(cr.totalCompletedCount), " +
        "   SUM(cr.attemptRouteCount), " +
        "   SUM(cr.avgDifficulty) " +
        ") " +
        "FROM ClimbingRecord cr " +
        "WHERE cr.climbingDate BETWEEN :startDate AND :endDate")
    ClimbingRecordStatisticsInfo getStatisticsInfoBetween(@Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

}