package com.climeet.climeet_backend.domain.climbinggym;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClimbingGymRepository extends JpaRepository<ClimbingGym, Long> {

    Optional<ClimbingGym> findById(Long gymId);

    Optional<ClimbingGym> findByName(String gymName);

    List<ClimbingGym> findByNameContaining(String word);

    List<ClimbingGym> findByNameContainingAndManagerIsNotNull(String word);
}