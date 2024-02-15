package com.climeet.climeet_backend.domain.board.dto;

import com.climeet.climeet_backend.domain.board.Board;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class boardRequestDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PostBoardRequest {

        private Long adminId;
        private String title;
        private String content;
        private List<String> imgUrls;
    }
}