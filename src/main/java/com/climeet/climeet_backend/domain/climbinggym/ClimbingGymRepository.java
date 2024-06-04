package com.climeet.climeet_backend.domain.climbinggym;

import io.lettuce.core.dynamic.annotation.Param;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ClimbingGymRepository extends JpaRepository<ClimbingGym, Long> {

    Optional<ClimbingGym> findByName(String gymName);

    Page<ClimbingGym> findByNameContaining(String gymName, Pageable pageable);

    @Query("SELECT cg FROM ClimbingGym cg LEFT JOIN Manager m on cg.id = m.climbingGym.id WHERE cg.name LIKE %:name% AND m.id IS NOT NULL")
    Page<ClimbingGym> findByNameContainingAndManagerIsNotNull(@Param("name") String name, Pageable pageable);
}