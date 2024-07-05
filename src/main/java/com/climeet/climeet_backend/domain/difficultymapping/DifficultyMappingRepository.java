package com.climeet.climeet_backend.domain.difficultymapping;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface DifficultyMappingRepository extends JpaRepository<DifficultyMapping, Long> {

    List<DifficultyMapping> findByIdIn(List<Long> difficultyIdList);

    DifficultyMapping findByClimbingGymAndDifficulty(ClimbingGym climbingGym, int difficulty);

    DifficultyMapping findByClimbingGymAndGymDifficultyName(ClimbingGym climbingGym, String gymDifficultyName);

    List<DifficultyMapping> findByClimbingGymOrderByDifficultyAsc(ClimbingGym climbingGym);

    @Query("SELECT dm "
        + "FROM DifficultyMapping dm "
        + "WHERE dm.climbingGym.id = :gymId "
        + "AND dm.difficulty >= 0 "
        + "ORDER BY dm.difficulty ASC ")
    List<DifficultyMapping> findDifficultyWithNoCompetition(ClimbingGym climbingGym);

}