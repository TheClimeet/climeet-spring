package com.climeet.climeet_backend.domain.climber;

import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClimberRepository  extends JpaRepository<Climber, Long> {

    Optional<Climber> findBySocialIdAndSocialType(String socialId, SocialType socialType);
    Optional<Climber> findByAccessTokenAndSocialType(String accessToken, SocialType socialType);
}
