package com.climeet.climeet_backend.domain.route;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteRepository extends JpaRepository<Route, Long> {
    Optional<Route> findById(Long id);
    List<Route> findBySectorClimbingGymId(Long gymId);
}