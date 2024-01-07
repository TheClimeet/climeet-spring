package com.climeet.climeet_backend.domain.route.dto;

import com.climeet.climeet_backend.domain.route.Route;
import lombok.*;

public class RouteResponseDto {


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteGetResponseDto {

        private Long sectorId;
        private String sectorName;
        private Long routeId;
        private String routeName;
        private int difficulty;
        private String routeImageUrl;

        public RouteGetResponseDto(Route route) {
            this.sectorId = route.getSector().getId();
            this.sectorName = route.getSector().getSectorName();
            this.routeId = route.getId();
            this.routeName = route.getName();
            this.difficulty = route.getDifficulty();
            this.routeImageUrl = route.getRouteImageUrl();
        }
    }
}
