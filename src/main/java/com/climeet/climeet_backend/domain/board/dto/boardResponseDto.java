package com.climeet.climeet_backend.domain.board.dto;

import com.climeet.climeet_backend.domain.board.Board;
import com.climeet.climeet_backend.domain.boardImage.BoardImage;
import com.climeet.climeet_backend.domain.user.User;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class boardResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardRetoolSimpleInfo {

        private LocalDateTime createdAt;
        private String title;
        private int viewCount;
        private int likeCount;
        public static BoardRetoolSimpleInfo toDTO(Board board) {
            return BoardRetoolSimpleInfo.builder()
                .createdAt(board.getCreatedAt())
                .title(board.getTitle())
                .viewCount(board.getViewCount())
                .likeCount(board.getLikeCount())
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardSimpleInfo {

        private Long boardId;
        private LocalDateTime createdAt;
        private int likeCount;
        private String title;
        private String content;
        private String profileImageUrl;
        private String image;

        public static BoardSimpleInfo toDTO(Board board, String thumbnailImageUrl, User user) {
            return BoardSimpleInfo.builder()
                .boardId(board.getId())
                .createdAt(board.getCreatedAt())
                .likeCount(board.getLikeCount())
                .title(board.getTitle())
                .content(board.getContent())
                .profileImageUrl(user.getProfileImageUrl())
                .image(thumbnailImageUrl)
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardDetailInfo {

        private Long boardId;
        private String title;
        private LocalDateTime createdAt;
        private String profileImageUrl;
        private String profileName;
        private Long followerCount;
        private Long followingCount;
        private String content;
        private int likeCount;
        private List<String> imageList;
        private Boolean likeStatus;

        public static BoardDetailInfo toDTO(Board board, List<String> boardImageList, User user, Boolean likeStatus) {
            return BoardDetailInfo.builder()
                .boardId(board.getId())
                .title(board.getTitle())
                .createdAt(board.getCreatedAt())
                .profileImageUrl(user.getProfileImageUrl())
                .profileName(user.getProfileName())
                .followerCount(user.getFollowerCount())
                .followingCount(user.getFollowingCount())
                .content(board.getContent())
                .likeCount(board.getLikeCount())
                .imageList(boardImageList)
                .likeStatus(likeStatus)
                .build();
        }
    }
}
