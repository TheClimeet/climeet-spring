package com.climeet.climeet_backend.domain.routerecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class RouteRecordRequestDto {

    // TODO: 2024/01/07 ClimbingRecord의 avgDifficulty 어떻게 구현 할 지 고민
    @Getter
    @NoArgsConstructor
    public static class CreateRouteRecordDto {

        @Schema(example = "1", description = "루트 Id")
        private Long routeId;

        @Schema(example = "10", description = "해당 루트 도전 횟수")
        private int attemptCount;

        @Schema(example = "1", description = "해당 루트 완등 여부")
        private Boolean isCompleted;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateRouteRecordDto {

        @Schema(example = "1", description = "루트 Id")
        private Long routeId;

        @Schema(example = "10", description = "해당 루트 도전 횟수")
        private Integer attemptCount;

        @Schema(example = "1", description = "해당 루트 완등 여부")
        private Boolean isComplete;
    }
}
