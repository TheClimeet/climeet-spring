package com.climeet.climeet_backend.domain.reportedshorts;

import com.climeet.climeet_backend.domain.shorts.Shorts;
import com.climeet.climeet_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportedShortsRepository extends JpaRepository<ReportedShorts, Long> {

    boolean existsByShorts(Shorts shorts);

    boolean existsByUserAndShorts(User user, Shorts shorts);

}
