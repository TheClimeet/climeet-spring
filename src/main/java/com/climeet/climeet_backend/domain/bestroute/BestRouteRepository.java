package com.climeet.climeet_backend.domain.bestroute;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestRouteRepository extends JpaRepository<BestRoute, Long> {

    List<BestRoute> findAllByOrderByRankingAsc();
}
