package com.climeet.climeet_backend.domain.shortscomment;

import com.climeet.climeet_backend.domain.shorts.Shorts;
import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentRequestDto.CreateShortsCommentRequest;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
public class ShortsComment extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Shorts shorts;

    @NotNull
    private String content;

    @ManyToOne
    private ShortsComment parentComment;

    private int childCommentCount;

    private int likeCount;

    private int dislikeCount;

    public static ShortsComment toEntity(User user, CreateShortsCommentRequest createShortsCommentRequest,
        Shorts shorts) {
        return ShortsComment.builder()
            .content(createShortsCommentRequest.getContent())
            .user(user)
            .shorts(shorts)
            .build();
    }

    public void updateParentComment(ShortsComment parentComment) {
        this.parentComment = parentComment;
    }

    public void updateChildCommentCount() {
        this.childCommentCount++;
    }

    public boolean isParentComment() {
        return childCommentCount != 0;
    }
}