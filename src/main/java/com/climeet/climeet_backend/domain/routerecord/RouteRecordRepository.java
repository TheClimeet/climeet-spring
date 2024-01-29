package com.climeet.climeet_backend.domain.routerecord;

import com.climeet.climeet_backend.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RouteRecordRepository extends JpaRepository<RouteRecord, Long> {
    List<RouteRecord> findAllByClimbingRecordId(Long ClimbingRecordId);

    @Query("SELECT rr FROM RouteRecord rr WHERE rr.user = :user")
    List<RouteRecord> findAllByUser(@Param("user") User user);

    @Query("SELECT " +
        "   rr.route.difficultyMapping.gymDifficulty as difficulty, COUNT(*) as count " +
        "FROM RouteRecord rr " +
        "WHERE rr.routeRecordDate BETWEEN :startDate AND :endDate " +
        "GROUP BY rr.route.difficultyMapping.gymDifficulty")
    List<Map<Long,Long>> getRouteRecordDifficultyBetween(@Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);


}