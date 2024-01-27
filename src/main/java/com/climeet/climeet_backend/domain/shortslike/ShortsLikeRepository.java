package com.climeet.climeet_backend.domain.shortslike;

import com.climeet.climeet_backend.domain.shorts.Shorts;
import com.climeet.climeet_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortsLikeRepository extends JpaRepository<ShortsLike, Long> {

    boolean existsShortsLikeByUserAndShorts(User user, Shorts shorts);

    void deleteByUserAndShorts(User user, Shorts shorts);
}