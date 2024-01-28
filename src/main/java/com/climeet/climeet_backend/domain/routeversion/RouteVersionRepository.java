package com.climeet.climeet_backend.domain.routeversion;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RouteVersionRepository extends JpaRepository<RouteVersion, Long> {
    List<RouteVersion> findByClimbingGymOrderByTimePointDesc(ClimbingGym climbingGym);

    Optional<RouteVersion> findByClimbingGymAndTimePoint(ClimbingGym climbingGym, LocalDate timePoint);
}
