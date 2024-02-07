package com.climeet.climeet_backend.domain.routerecord;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

public interface RouteRecordRepository extends JpaRepository<RouteRecord, Long> {
    List<RouteRecord> findAllByClimbingRecordId(Long ClimbingRecordId);

    @Query("SELECT rr FROM RouteRecord rr WHERE rr.user = :user")
    List<RouteRecord> findAllByUser(@Param("user") User user);

    @Query("SELECT " +
        "   rr.route.difficultyMapping.difficulty as difficulty, COUNT(*) as count " +
        "FROM RouteRecord rr " +
        "WHERE rr.routeRecordDate BETWEEN :startDate AND :endDate AND rr.user = :user " +
        "GROUP BY rr.route.difficultyMapping.difficulty")
    List<Object[]> getRouteRecordDifficultyBetween(
        @Param("user") User user,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT " +
        "   rr.route.difficultyMapping.difficulty as difficulty, COUNT(*) as count " +
        "FROM RouteRecord rr " +
        "WHERE rr.routeRecordDate BETWEEN :startDate AND :endDate AND rr.gym = :gym AND rr.user = :user " +
        "GROUP BY rr.route.difficultyMapping.difficulty")
    List<Object[]> getRouteRecordDifficultyBetweenDatesAndGym(
        @Param("user") User user,
        @Param("gym") ClimbingGym gym,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT " +
        "   rr.route.difficultyMapping.difficulty as difficulty, COUNT(*) as count " +
        "FROM RouteRecord rr " +
        "WHERE rr.user = :user AND rr.isCompleted = true " +
        "GROUP BY rr.route.difficultyMapping.difficulty" )
    List<Object[]> findAllRouteRecordDifficultyAndUser(
        @Param("user") User user
    );

    @Query("SELECT " +
        "   rr.route.difficultyMapping.difficulty as difficulty, COUNT(*) as count " +
        "FROM RouteRecord rr " +
        "WHERE rr.user = :user AND rr.isCompleted = true AND rr.gym = :gym " +
        "GROUP BY rr.route.difficultyMapping.difficulty" )
    List<Object[]> findAllRouteRecordDifficultyAndUserAndGym(
        @Param("user") User user,
        @Param("gym") ClimbingGym gym
    );

    @Query("SELECT " +
        "   rr.route.difficultyMapping.difficulty as difficulty, COUNT(*) as count " +
        "FROM RouteRecord rr " +
        "WHERE rr.routeRecordDate BETWEEN :startDate AND :endDate AND rr.gym = :gym AND rr.isCompleted = true " +
        "GROUP BY rr.route.difficultyMapping.difficulty")
    List<Object[]> getRouteRecordDifficultyBetweenDaysAndGym(
        @Param("gym") ClimbingGym gym,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );
}