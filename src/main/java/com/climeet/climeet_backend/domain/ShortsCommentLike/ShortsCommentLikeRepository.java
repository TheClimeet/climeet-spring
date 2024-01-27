package com.climeet.climeet_backend.domain.ShortsCommentLike;

import com.climeet.climeet_backend.domain.shortscomment.ShortsComment;
import com.climeet.climeet_backend.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShortsCommentLikeRepository extends JpaRepository<ShortsCommentLike, Long> {

    @Query("SELECT scl.shortsComment.id, scl.commentLikeStatus FROM ShortsCommentLike scl WHERE scl.user = :user AND scl.shortsComment IN :comments")
    List<Object[]> findCommentLikeStatusByUserAndCommentsIn(@Param("user") User user, @Param("comments") List<ShortsComment> comments);

    Optional<ShortsCommentLike> findShortsCommentLikeByUserAndShortsComment(User user, ShortsComment shortsComment);
}