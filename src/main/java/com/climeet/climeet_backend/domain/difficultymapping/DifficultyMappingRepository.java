package com.climeet.climeet_backend.domain.difficultymapping;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DifficultyMappingRepository extends JpaRepository<DifficultyMapping, Long> {

    DifficultyMapping findByClimbingGymAndClimeetDifficulty(ClimbingGym climbingGym, int climeetDifficulty);

    DifficultyMapping findByClimbingGymAndGymDifficultyName(ClimbingGym climbingGym, String gymDifficultyName);

    List<DifficultyMapping> findByClimbingGymOrderByClimeetDifficultyAsc(ClimbingGym climbingGym);

}