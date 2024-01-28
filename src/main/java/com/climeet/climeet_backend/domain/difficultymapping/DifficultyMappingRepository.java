package com.climeet.climeet_backend.domain.difficultymapping;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DifficultyMappingRepository extends JpaRepository<DifficultyMapping, Long> {

    DifficultyMapping findByClimbingGymAndGymDifficulty(ClimbingGym climbingGym, int gymDifficulty);

}