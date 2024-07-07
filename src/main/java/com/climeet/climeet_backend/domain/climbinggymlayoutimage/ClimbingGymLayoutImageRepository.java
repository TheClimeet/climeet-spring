package com.climeet.climeet_backend.domain.climbinggymlayoutimage;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClimbingGymLayoutImageRepository extends JpaRepository<ClimbingGymLayoutImage, Long>{

    List<ClimbingGymLayoutImage> findByIdIn(List<Long> layoutImgIdList);
    List<ClimbingGymLayoutImage> findClimbingGymLayoutImageByClimbingGym(ClimbingGym climbingGym);
}
