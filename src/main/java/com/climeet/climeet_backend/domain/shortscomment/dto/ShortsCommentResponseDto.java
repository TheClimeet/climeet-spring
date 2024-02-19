package com.climeet.climeet_backend.domain.shortscomment.dto;

import com.climeet.climeet_backend.domain.shortscomment.CommentLikeStatus;
import com.climeet.climeet_backend.domain.shortscomment.ShortsComment;
import com.climeet.climeet_backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ShortsCommentResponseDto {

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ShortsCommentParentResponse {

        Long commentId;
        String nickname;
        String profileImageUrl;
        String content;
        CommentLikeStatus commentLikeStatus;
        int likeCount;
        int dislikeCount;
        Boolean isParent;
        Long parentCommentId;
        int childCommentCount;
        String createdDate;

        public static ShortsCommentParentResponse toDTO(
            User user, ShortsComment shortsComment,
            CommentLikeStatus commentLikeStatus, Long parentCommentId,
            int childCommentCount,
            String createdDate) {
            return ShortsCommentParentResponse.builder()
                .commentId(shortsComment.getId())
                .nickname(user.getProfileName())
                .profileImageUrl(user.getProfileImageUrl())
                .content(shortsComment.getContent())
                .commentLikeStatus(commentLikeStatus)
                .likeCount(shortsComment.getLikeCount())
                .dislikeCount(shortsComment.getDislikeCount())
                .isParent(shortsComment.getIsParent())
                .parentCommentId(parentCommentId)
                .childCommentCount(childCommentCount)
                .createdDate(createdDate)
                .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class ShortsCommentChildResponse {

        Long commentId;
        String nickname;
        String profileImageUrl;
        String content;
        CommentLikeStatus commentLikeStatus;
        int likeCount;
        int dislikeCount;
        Long parentCommentId;
        String createdDate;

        public static ShortsCommentChildResponse toDTO(Long commentId, String nickname,
            String profileImageUrl,
            String content, CommentLikeStatus commentLikeStatus,
            int likeCount, int dislikeCount,
            Long parentCommentId, String createdDate) {
            return ShortsCommentChildResponse.builder()
                .commentId(commentId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .content(content)
                .commentLikeStatus(commentLikeStatus)
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .parentCommentId(parentCommentId)
                .createdDate(createdDate)
                .build();
        }
    }
}