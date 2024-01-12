package com.climeet.climeet_backend.domain.climber;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClimberRepository  extends JpaRepository<Climber, Long> {

    Climber findBySocialId(Long socialId);
}