package com.climeet.climeet_backend.domain.bestclimber.clear;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestClearClimberRepository extends JpaRepository<BestClearClimber, Long> {
    List<BestClearClimber> findAllByOrderByRankingAsc();
}
