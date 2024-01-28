package com.climeet.climeet_backend.domain.route.dto;

import com.climeet.climeet_backend.domain.route.Route;
import lombok.*;

public class RouteResponseDto {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteSimpleResponse {

        private Long routeId;
        private Long sectorId;
        private String sectorName;
        private int difficulty;
        private String routeImageUrl;

        public static RouteSimpleResponse toDto(Route route) {
            return RouteSimpleResponse.builder()
                .routeId(route.getId())
                .sectorId(route.getSector().getId())
                .sectorName(route.getSector().getSectorName())
                .difficulty(route.getDifficulty())
                .routeImageUrl(route.getRouteImageUrl())
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteIdSimpleResponse {

        private Long routeId;

        public static RouteIdSimpleResponse toDto(Route route) {
            return RouteIdSimpleResponse.builder()
                .routeId(route.getId())
                .build();
        }
    }
}
