package com.climeet.climeet_backend.domain.routerecord;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.user.User;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface RouteRecordRepository extends JpaRepository<RouteRecord, Long> {

    List<RouteRecord> findAllByClimbingRecordId(Long ClimbingRecordId);

    @Query("SELECT rr FROM RouteRecord rr WHERE rr.user = :user")
    List<RouteRecord> findAllByUser(@Param("user") User user);

    @Query("SELECT " +
        "   rr.difficulty as difficulty, COUNT(*) as count " +
        "FROM RouteRecord rr " +
        "WHERE rr.routeRecordDate BETWEEN :startDate AND :endDate AND rr.user = :user AND rr.isCompleted = true " +
        "GROUP BY rr.difficulty")
    List<Object[]> getRouteRecordDifficultyBetween(
        @Param("user") User user,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT " +
        "   rr.difficulty as difficulty, COUNT(*) as count " +
        "FROM RouteRecord rr " +
        "WHERE rr.routeRecordDate BETWEEN :startDate AND :endDate AND rr.gym = :gym AND rr.user = :user AND rr.isCompleted = true " +
        "GROUP BY rr.difficulty")
    List<Object[]> getRouteRecordDifficultyBetweenDatesAndGym(
        @Param("user") User user,
        @Param("gym") ClimbingGym gym,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT " +
        "   rr.difficulty as difficulty, COUNT(*) as count " +
        "FROM RouteRecord rr " +
        "WHERE rr.user = :user AND rr.isCompleted = true " +
        "AND rr.difficulty IS NOT NULL " +
        "GROUP BY rr.difficulty" )
    List<Object[]> findAllRouteRecordDifficultyAndUser(
        @Param("user") User user
    );

    @Query("SELECT " +
        "   rr.difficulty as difficulty, COUNT(*) as count " +
        "FROM RouteRecord rr " +
        "WHERE rr.user = :user AND rr.isCompleted = true AND rr.gym = :gym " +
        "AND rr.difficulty IS NOT NULL " +
        "GROUP BY rr.difficulty" )
    List<Object[]> findAllRouteRecordDifficultyAndUserAndGym(
        @Param("user") User user,
        @Param("gym") ClimbingGym gym
    );

    @Query("SELECT " +
        "   rr.difficulty as difficulty, COUNT(*) as count " +
        "FROM RouteRecord rr " +
        "WHERE rr.routeRecordDate BETWEEN :startDate AND :endDate AND rr.gym = :gym AND rr.isCompleted = true " +
        "GROUP BY rr.difficulty")
    List<Object[]> getRouteRecordDifficultyBetweenDaysAndGym(
        @Param("gym") ClimbingGym gym,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate
    );

    @Query("SELECT " +
        "   AVG(rr.route.difficultyMapping.difficulty) as averageDifficulty " +
        "FROM RouteRecord rr " +
        "JOIN FollowRelationship fr ON rr.user = fr.follower " +
        "WHERE rr.isCompleted = true AND fr.following = :manager " +
        "AND rr.route.difficultyMapping.difficulty IS NOT NULL " +
        "GROUP BY rr.user")
    List<Float> getFollowUserSumCountDifficultyInClimbingGym(@Param("manager") User manager);

    @Query("SELECT " +
        "   AVG(rr.difficulty) as averageDifficulty " +
        "FROM RouteRecord rr " +
        "WHERE rr.user = :user " +
        "AND rr.difficulty IS NOT NULL")
    Optional<Float> findAverageDifficultyByUser(@Param("user") User user);

}