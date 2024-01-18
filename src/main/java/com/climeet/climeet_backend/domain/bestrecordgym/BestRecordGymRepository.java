package com.climeet.climeet_backend.domain.bestrecordgym;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BestRecordGymRepository extends JpaRepository<BestRecordGym, Long> {
    List<BestRecordGym> findAllByOrderByRankingAsc();
}
