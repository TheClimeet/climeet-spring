package com.climeet.climeet_backend.domain.board.dto;

import com.climeet.climeet_backend.domain.board.Board;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class boardResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardSimpleInfo {

        private LocalDateTime createdAt;
        private String title;
        private int viewCount;
        private int likeCount;
        public static BoardSimpleInfo toDTO(Board board) {
            return BoardSimpleInfo.builder()
                .createdAt(board.getCreatedAt())
                .title(board.getTitle())
                .viewCount(board.getViewCount())
                .likeCount(board.getLikeCount())
                .build();
        }
    }
}
