package com.climeet.climeet_backend.domain.shortscomment.dto;

import com.climeet.climeet_backend.domain.shortscomment.CommentLikeStatus;
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

        public static ShortsCommentParentResponse toDto(Long commentId, String nickname,
            String profileImageUrl,
            String content, CommentLikeStatus commentLikeStatus, Boolean isParent,
            int likeCount, int dislikeCount,
            Long parentCommentId, int childCommentCount, String createdDate) {
            return ShortsCommentParentResponse.builder()
                .commentId(commentId)
                .nickname(nickname)
                .profileImageUrl(profileImageUrl)
                .content(content)
                .commentLikeStatus(commentLikeStatus)
                .likeCount(likeCount)
                .dislikeCount(dislikeCount)
                .isParent(isParent)
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

        public static ShortsCommentChildResponse toDto(Long commentId, String nickname,
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