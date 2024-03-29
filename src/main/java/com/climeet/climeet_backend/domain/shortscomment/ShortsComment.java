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
import lombok.Builder.Default;
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

    private int childCommentCount = 0;

    private int likeCount = 0;

    private int dislikeCount = 0;

    @Default
    private Boolean isFirstChild = true;

    @Default
    private Boolean isParent = true;

    public static ShortsComment toEntity(User user, CreateShortsCommentRequest createShortsCommentRequest,
        Shorts shorts) {
        return ShortsComment.builder()
            .content(createShortsCommentRequest.getContent())
            .user(user)
            .shorts(shorts)
            .build();
    }

    public void updateIsParentFalse() {
        this.isParent = false;
    }

    public void updateParentComment(ShortsComment parentComment) {
        this.parentComment = parentComment;
    }

    public void updateChildCommentCount() {
        this.childCommentCount++;
    }

    public void updateIsFirstChildFalse() {
        this.isFirstChild = false;
    }

    public void updateLikeCountPlus() {
        this.likeCount++;
    }
    public void updateLikeCountMinus() {
        this.likeCount--;
    }
    public void updateDislikeCountPlus() {
        this.dislikeCount++;
    }
    public void updateDislikeCountMinus() {
        this.dislikeCount--;
    }
}