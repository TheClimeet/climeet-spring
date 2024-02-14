package com.climeet.climeet_backend.domain.route.dto;

import com.climeet.climeet_backend.domain.route.Route;
import lombok.*;

public class RouteResponseDto {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteDetailResponse {

        private Long routeId;
        private Long sectorId;
        private String sectorName;
        private String climeetDifficultyName;
        private int difficulty;
        private String gymDifficultyName;
        private String gymDifficultyColor;
        private String routeImageUrl;
        private String holdColor;

        public static RouteDetailResponse toDTO(Route route) {
            return RouteDetailResponse.builder()
                .routeId(route.getId())
                .sectorId(route.getSector().getId())
                .sectorName(route.getSector().getSectorName())
                .climeetDifficultyName(route.getDifficultyMapping().getClimeetDifficultyName())
                .difficulty(route.getDifficultyMapping().getDifficulty())
                .gymDifficultyName(route.getDifficultyMapping().getGymDifficultyName())
                .gymDifficultyColor(route.getDifficultyMapping().getGymDifficultyColor())
                .routeImageUrl(route.getRouteImageUrl())
                .holdColor(route.getHoldColor())
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteSimpleResponse {

        private Long routeId;

        public static RouteSimpleResponse toDTO(Route route) {
            return RouteSimpleResponse.builder()
                .routeId(route.getId())
                .build();
        }
    }
}
