package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.user.User;
import jakarta.persistence.Tuple;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClimbingRecordRepository extends JpaRepository<ClimbingRecord, Long> {

    List<ClimbingRecord> findByClimbingDateBetweenAndUser(LocalDate startDate, LocalDate endDate,
        User user);

    Optional<ClimbingRecord> findById(Long id);

    @Query("SELECT " +
        "   SUM(HOUR(cr.climbingTime) * 3600 + MINUTE(cr.climbingTime) * 60 + SECOND(cr.climbingTime)) as totalTime, "
        +
        "   SUM(cr.totalCompletedCount) as totalCompletedCount, " +
        "   SUM(cr.attemptRouteCount) as attemptRouteCount " +
        "FROM ClimbingRecord cr " +
        "WHERE cr.climbingDate BETWEEN :startDate AND :endDate AND cr.user = :user")
    Tuple getStatisticsInfoBetweenDaysAndUser(@Param("user") User user,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);

    @Query("SELECT " +
        "   SUM(HOUR(cr.climbingTime) * 3600 + MINUTE(cr.climbingTime) * 60 + SECOND(cr.climbingTime)) as totalTime, "
        +
        "   SUM(cr.totalCompletedCount) as totalCompletedCount, " +
        "   SUM(cr.attemptRouteCount) as attemptRouteCount " +
        "FROM ClimbingRecord cr " +
        "WHERE cr.climbingDate BETWEEN :startDate AND :endDate AND cr.gym = :climbingGym AND cr.user = :user")
    Tuple getStatisticsInfoBetweenDaysAndUserAndGym(@Param("user") User user,
        @Param("climbingGym" ) ClimbingGym climbingGym,
        @Param("startDate") LocalDate startDate,
        @Param("endDate") LocalDate endDate);



    @Query("SELECT " +
        "   SUM(cr.totalCompletedCount) as totalCompletedCount, " +
        "   SUM(cr.attemptRouteCount) as attemptRouteCount " +
        "FROM ClimbingRecord cr " +
        "WHERE cr.user = :user")
    Tuple findAllClearRateAndUser(@Param("user") User user);

    @Query("SELECT " +
        "   SUM(cr.totalCompletedCount) as totalCompletedCount, " +
        "   SUM(cr.attemptRouteCount) as attemptRouteCount " +
        "FROM ClimbingRecord cr " +
        "WHERE cr.user = :user AND cr.gym = :gym ")
    Tuple findAllClearRateAndUserAndGym(@Param("user") User user, @Param("gym") ClimbingGym gym);

    @Query("SELECT " +
        "cr.user, SUM(cr.totalCompletedCount) as totalCount " +
        "FROM ClimbingRecord cr " +
        "WHERE cr.climbingDate BETWEEN :startDate AND :endDate " +
        "AND cr.gym = :climbingGym " +
        "GROUP BY cr.user " +
        "ORDER BY totalCount DESC")
    List<Object[]> findByClearRankingClimbingDateBetweenAndClimbingGym(LocalDate startDate,
        LocalDate endDate, ClimbingGym climbingGym);

    @Query("SELECT " +
        "cr.user, SUM(HOUR(cr.climbingTime) * 3600 + MINUTE(cr.climbingTime) * 60 + SECOND(cr.climbingTime)) as totalTime " +
        "FROM ClimbingRecord cr " +
        "WHERE cr.climbingDate BETWEEN :startDate AND :endDate " +
        "AND cr.gym = :climbingGym " +
        "GROUP BY cr.user " +
        "ORDER BY totalTime DESC")
    List<Object[]> findByTimeRankingClimbingDateBetweenAndClimbingGym(LocalDate startDate,
        LocalDate endDate, ClimbingGym climbingGym);

    @Query(
        "SELECT cr.user, MAX(cr.highDifficulty), COUNT(*) as highDifficultyCount " +
         "FROM " +
            "(" +
            "SELECT cr.user as user, MAX(cr.highDifficulty) as highDifficulty " +
            "FROM ClimbingRecord cr " +
            "WHERE cr.climbingDate BETWEEN :startDate AND :endDate AND cr.gym = :climbingGym " +
            "GROUP BY cr.user) as subquery " +
        "INNER JOIN ClimbingRecord cr ON cr.user.id = subquery.user.id " +
        "Where subquery.user.id = cr.user.id AND subquery.highDifficulty = cr.highDifficulty " +
        "AND cr.climbingDate BETWEEN :startDate AND :endDate AND cr.gym = :climbingGym " +
        "GROUP BY subquery.user.id " +
        "ORDER BY subquery.highDifficulty DESC, highDifficultyCount DESC"
    )
    List<Object[]> findByLevelRankingClimbingDateBetweenAndClimbingGym(LocalDate startDate,
        LocalDate endDate, ClimbingGym climbingGym);

    @Query(
        "SELECT cr.gym.id, cr.gym.name "
            + "FROM ClimbingRecord cr "
            + "WHERE cr.user = :user and cr.climbingDate BETWEEN :startDate AND :endDate "
            + "GROUP BY cr.gym.id"
    )
    List<Object[]> findGymListByUserAndClimbingDateBetween(User user, LocalDate startDate, LocalDate endDate);

    @Query(
        "SELECT cr.gym.id, cr.gym.name "
            + "FROM ClimbingRecord cr "
            + "WHERE cr.user = :user "
            + "GROUP BY cr.gym.id"
    )
    List<Object[]> findGymListByUser(User user);
}