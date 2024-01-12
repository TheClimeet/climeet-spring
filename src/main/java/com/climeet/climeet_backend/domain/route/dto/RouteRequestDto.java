package com.climeet.climeet_backend.domain.route.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class RouteRequestDto {

    @Getter
    @NoArgsConstructor
    public static class RouteCreateRequestDto {

        private Long sectorId;
        private String name;
        private int difficulty;
        private String routeImageUrl;
    }
}
