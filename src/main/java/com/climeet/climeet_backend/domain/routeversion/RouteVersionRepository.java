package com.climeet.climeet_backend.domain.routeversion;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.route.Route;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteVersionRepository extends JpaRepository<RouteVersion, Long> {

    @Query("SELECT rv.timePoint "
        + "FROM RouteVersion rv "
        + "WHERE rv.climbingGym.id = :gymId "
        + "ORDER BY rv.timePoint desc")
    List<LocalDate> findTimePointListByGymId(@Param("gymId") Long gymId);

    Optional<RouteVersion> findFirstByClimbingGymAndTimePointLessThanEqualOrderByTimePointDesc(ClimbingGym climbingGym, LocalDate timePoint);

    @Query("SELECT rv.climbData "
        + "FROM RouteVersion rv "
        + "WHERE rv.climbingGym = :climbingGym "
        + "AND rv.timePoint <= :timePoint "
        + "ORDER BY rv.timePoint "
        + "LIMIT 1")
    Map<String, List<Long>> findClimbDataByClimbingGymAndTimePoint(@Param("climbingGym") ClimbingGym climbingGym, @Param("timePoint") LocalDate timePoint);

    Optional<RouteVersion> findByClimbingGymAndTimePoint(ClimbingGym climbingGym, LocalDate timePoint);
}
