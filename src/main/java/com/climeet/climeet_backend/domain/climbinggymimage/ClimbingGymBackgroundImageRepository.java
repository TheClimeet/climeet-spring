package com.climeet.climeet_backend.domain.climbinggymimage;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClimbingGymBackgroundImageRepository extends JpaRepository<ClimbingGymBackgroundImage, Long>{
    List<ClimbingGymBackgroundImage> findByClimbingGym(ClimbingGym climbingGym);
}
