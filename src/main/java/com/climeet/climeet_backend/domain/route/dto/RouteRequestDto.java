package com.climeet.climeet_backend.domain.route.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class RouteRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateRouteRequest {

        private Long sectorId;
        private int gymDifficulty;
    }
}