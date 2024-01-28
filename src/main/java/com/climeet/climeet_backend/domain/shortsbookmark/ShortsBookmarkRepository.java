package com.climeet.climeet_backend.domain.shortsbookmark;

import com.climeet.climeet_backend.domain.shorts.Shorts;
import com.climeet.climeet_backend.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortsBookmarkRepository extends JpaRepository<ShortsBookmark, Long> {

    boolean existsShortsBookmarkByUserAndShorts(User user, Shorts shorts);

    Optional<ShortsBookmark> findByUserAndShorts(User user, Shorts shorts);
}