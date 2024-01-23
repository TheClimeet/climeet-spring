package com.climeet.climeet_backend.domain.bestclimber.time;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestTimeClimberRepository extends JpaRepository<BestTimeClimber, Long> {
    List<BestTimeClimber> findAllByOrderByRankingAsc();
}
