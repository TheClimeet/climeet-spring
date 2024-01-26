package com.climeet.climeet_backend.domain.climbinggym;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClimbingGymRepository extends JpaRepository<ClimbingGym, Long> {

    Optional<ClimbingGym> findByName(String gymName);

    Page<ClimbingGym> findByNameContaining(String gymName, Pageable pageable);

    Page<ClimbingGym> findByNameContainingAndManagerIsNotNull(String gymName, Pageable pageable);
}