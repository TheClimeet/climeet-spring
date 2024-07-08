package com.climeet.climeet_backend.domain.reportedComment;

import com.climeet.climeet_backend.domain.shortscomment.ShortsComment;
import com.climeet.climeet_backend.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportedCommentRepository extends JpaRepository<ReportedComment, Long> {

    boolean existsByUserAndShortsComment(User user, ShortsComment shortsComment);

}
