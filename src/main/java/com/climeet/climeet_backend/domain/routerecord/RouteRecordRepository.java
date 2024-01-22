package com.climeet.climeet_backend.domain.routerecord;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RouteRecordRepository extends JpaRepository<RouteRecord, Long> {
    List<RouteRecord> findAllByClimbingRecordId(Long ClimbingRecordId);

    @Query("SELECT " +
        "   rr.route.difficulty as difficulty, COUNT(*) as count " +
        "FROM RouteRecord rr " +
        "WHERE rr.routeRecordDate BETWEEN :startDate AND :endDate " +
        "GROUP BY rr.route.difficulty")
    List<Map<Long,Long>> getRouteRecordDifficultyBetween(@Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);


}