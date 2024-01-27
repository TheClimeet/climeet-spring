package com.climeet.climeet_backend.domain.shortsbookmark;

import com.climeet.climeet_backend.domain.shorts.Shorts;
import com.climeet.climeet_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShortsBookmarkRepository extends JpaRepository<ShortsBookmark, Long> {

    boolean existsShortsBookmarkByUserAndShorts(User user, Shorts shorts);

    void deleteByUserAndShorts(User user, Shorts shorts);
}