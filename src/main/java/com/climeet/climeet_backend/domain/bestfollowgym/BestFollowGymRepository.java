package com.climeet.climeet_backend.domain.bestfollowgym;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestFollowGymRepository extends JpaRepository<BestFollowGym, Long> {
    List<BestFollowGym> findAllByOrderByRankingAsc();
}
