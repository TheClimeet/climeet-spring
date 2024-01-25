package com.climeet.climeet_backend.domain.ShortsCommentLike;

import com.climeet.climeet_backend.domain.shortscomment.CommentLikeStatus;
import com.climeet.climeet_backend.domain.shortscomment.ShortsComment;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Entity
public class ShortsCommentLike extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private ShortsComment shortsComment;

    @Enumerated(EnumType.STRING)
    private CommentLikeStatus commentLikeStatus;

    public static ShortsCommentLike toEntity(User user, ShortsComment shortsComment,
        CommentLikeStatus commentLikeStatus) {
        return ShortsCommentLike.builder()
            .user(user)
            .shortsComment(shortsComment)
            .commentLikeStatus(commentLikeStatus)
            .build();
    }

    public void updateCommentLikeStatus(CommentLikeStatus commentLikeStatus) {
        this.commentLikeStatus = commentLikeStatus;
    }
}