package com.climeet.climeet_backend.domain.difficultymapping;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DifficultyMappingRepository extends JpaRepository<DifficultyMapping, Long> {

    List<DifficultyMapping> findByIdIn(List<Long> difficultyIdList);

    DifficultyMapping findByClimbingGymAndDifficulty(ClimbingGym climbingGym, int difficulty);

    DifficultyMapping findByClimbingGymAndGymDifficultyName(ClimbingGym climbingGym, String gymDifficultyName);

    List<DifficultyMapping> findByClimbingGymOrderByDifficultyAsc(ClimbingGym climbingGym);

    List<DifficultyMapping> findByClimbingGymAndDifficultyIsNotNullOrderByDifficultyAsc(ClimbingGym climbingGym);

}