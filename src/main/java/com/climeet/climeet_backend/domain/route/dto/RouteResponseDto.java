package com.climeet.climeet_backend.domain.route.dto;

import com.climeet.climeet_backend.domain.route.Route;
import lombok.*;

public class RouteResponseDto {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteSimpleResponse {

        private Long sectorId;
        private String sectorName;
        private Long routeId;
        private String routeName;
        private String difficulty;
        private String routeImageUrl;

        public RouteSimpleResponse(Route route) {
            this.sectorId = route.getSector().getId();
            this.sectorName = route.getSector().getSectorName();
            this.routeId = route.getId();
            this.routeName = route.getName();
            this.difficulty = route.getDifficultyMapping().getGymDifficultyName();
            this.routeImageUrl = route.getRouteImageUrl();
        }
    }
}
