package com.climeet.climeet_backend.domain.climbinggymimage;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClimbingGymBackgroundImageRepository extends JpaRepository<ClimbingGymBackgroundImage, Long>{
    Optional<ClimbingGymBackgroundImage> findByClimbingGym(ClimbingGym climbingGym);

    @Query("SELECT c.imgUrl FROM ClimbingGymBackgroundImage c WHERE c.climbingGym = :climbingGym")
    Optional<String> findImgUrlByClimbingGym(@Param("climbingGym") ClimbingGym climbingGym);
}
