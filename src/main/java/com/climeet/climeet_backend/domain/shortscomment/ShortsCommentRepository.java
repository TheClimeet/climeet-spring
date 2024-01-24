package com.climeet.climeet_backend.domain.shortscomment;

import com.climeet.climeet_backend.domain.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ShortsCommentRepository extends JpaRepository<ShortsComment, Long> {


    @Query("SELECT sc FROM ShortsComment sc WHERE sc.shorts.id = :shortsId AND sc.parentComment IS NULL ORDER BY sc.createdAt ASC")
    Optional<Slice<ShortsComment>> findParentCommentsByShortsIdOrderedByCreatedAtAsc(
        @Param("shortsId") Long shortsId, Pageable pageable);

    @Query("SELECT sc FROM ShortsComment sc WHERE sc.parentComment.id = :parentId AND sc.id != :parentId ORDER BY sc.createdAt ASC limit 1")
    Optional<ShortsComment> findFirstChildCommentByIdAndNotParentOrderByCreatedAtAsc(
        @Param("parentId") Long parentId);

    Optional<Slice<ShortsComment>> findChildCommentsByShortsIdAndParentCommentIdAndIsFirstChildFalseOrderByCreatedAtAsc(
        Long shortsId, Long parentCommentId, Pageable pageable);
}