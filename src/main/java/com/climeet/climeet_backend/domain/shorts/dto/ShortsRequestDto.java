package com.climeet.climeet_backend.domain.shorts.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShortsRequestDto {

    @Getter
    @NoArgsConstructor
    public static class PostShortsReq {

        private Long climbingGymId;
        private Long routeId;
        private Long sectorId;
        private String description;
        boolean isSoundEnabled;
        boolean isPublic;
    }
}