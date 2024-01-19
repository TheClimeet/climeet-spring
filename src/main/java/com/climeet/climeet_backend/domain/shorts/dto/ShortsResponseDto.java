package com.climeet.climeet_backend.domain.shorts.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShortsResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortsSimpleInfo {

        private String thumbnailImageUrl;
        private String gymName;
        private int difficulty;

        public static ShortsSimpleInfo toDTO(String thumbnailImageUrl, String gymName,
            int difficulty) {
            return ShortsSimpleInfo.builder()
                .thumbnailImageUrl(thumbnailImageUrl)
                .gymName(gymName)
                .difficulty(difficulty)
                .build();
        }
    }
}