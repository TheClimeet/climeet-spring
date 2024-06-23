package com.climeet.climeet_backend.domain.review;

import com.climeet.climeet_backend.domain.climber.Climber;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByClimbingGymAndClimberId(ClimbingGym climbingGym, Long userId);

    Page<Review> findByClimbingGymAndClimberIdIsNotOrderByUpdatedAtDesc(ClimbingGym climbingGym,
        Long userId, Pageable pageable);
}