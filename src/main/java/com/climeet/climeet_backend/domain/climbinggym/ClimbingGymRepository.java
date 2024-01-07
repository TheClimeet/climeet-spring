package com.climeet.climeet_backend.domain.climbinggym;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClimbingGymRepository extends JpaRepository<ClimbingGym, Long> {

    Optional<ClimbingGym> findById(Long gymId);
}