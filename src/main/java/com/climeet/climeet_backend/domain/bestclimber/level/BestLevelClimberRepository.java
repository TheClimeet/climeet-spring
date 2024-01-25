package com.climeet.climeet_backend.domain.bestclimber.level;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestLevelClimberRepository extends JpaRepository<BestLevelClimber, Long> {
    List<BestLevelClimber> findAllByOrderByRankingAsc();
}
