package com.climeet.climeet_backend.domain.route;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
    List<Route> findBySectorClimbingGymId(Long gymId);

    List<Route> findBySectorId(Long sectorId);
}