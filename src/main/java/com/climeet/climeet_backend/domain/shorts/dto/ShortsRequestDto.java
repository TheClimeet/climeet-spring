package com.climeet.climeet_backend.domain.shorts.dto;

import com.climeet.climeet_backend.domain.shorts.ShortsVisibility;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShortsRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateShortsRequest {

        private Long climbingGymId;
        private Long routeId;
        private Long sectorId;
        private String description;
        boolean isSoundEnabled;
        ShortsVisibility shortsVisibility;
    }
}