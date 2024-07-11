package com.climeet.climeet_backend.domain.blockeduser;

import com.climeet.climeet_backend.domain.user.User;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlockedUserRepository extends JpaRepository<BlockedUser, Long> {

    boolean existsByBlockerAndBlocked(User blocker, User blocked);
    List<BlockedUser> findByBlocker(User blocker);
}
