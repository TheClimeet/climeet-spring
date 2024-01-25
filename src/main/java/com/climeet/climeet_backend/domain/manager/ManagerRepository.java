package com.climeet.climeet_backend.domain.manager;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    //Optional<Manager> findByName(String gymName);
    Optional<Manager> findByLoginId(String loginId);
    Optional<Manager> findByClimbingGym(ClimbingGym climbingGym);

    boolean existsByClimbingGym(ClimbingGym gym);

    Optional<Manager> findByLoginIdAndPassword(String loginId, String password);

}