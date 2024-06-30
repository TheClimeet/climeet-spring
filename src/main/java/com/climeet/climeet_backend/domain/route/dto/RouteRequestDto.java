package com.climeet.climeet_backend.domain.route.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RouteRequestDto {

    @Getter
    @AllArgsConstructor
    public static class CreateRouteRequest {

        private String sectorName;
        private String gymDifficultyName;
        private String holdColor;
        private String imgUrl;
    }
}