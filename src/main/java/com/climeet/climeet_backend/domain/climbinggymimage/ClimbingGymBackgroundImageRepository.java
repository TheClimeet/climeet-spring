package com.climeet.climeet_backend.domain.climbinggymimage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClimbingGymBackgroundImageRepository extends JpaRepository<ClimbingGymBackgroundImage, Long>{

}
