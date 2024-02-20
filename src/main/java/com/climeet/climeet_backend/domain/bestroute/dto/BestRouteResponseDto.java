package com.climeet.climeet_backend.domain.bestroute.dto;

import com.climeet.climeet_backend.domain.bestrecordgym.dto.BestRecordGymResponseDto.BestRecordGymDetailInfo;
import com.climeet.climeet_backend.domain.bestroute.BestRoute;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BestRouteResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BestRouteDetailInfo {

        private Long routeId;
        private int ranking;
        private int thisWeekSelectionCount;
        private String routeImageUrl;
        private String gymName;
        private String sectorName;
        private String climeetDifficultyName;
        private String gymDifficultyName;
        private String gymDifficultyColor;
        private String holdColor;

        public static BestRouteDetailInfo toDTO(BestRoute bestRoute) {
            return BestRouteDetailInfo.builder()
                .routeId(bestRoute.getRoute().getId())
                .ranking(bestRoute.getRanking())
                .thisWeekSelectionCount(bestRoute.getThisWeekSelectionCount())
                .routeImageUrl(bestRoute.getRouteImageUrl())
                .gymName(bestRoute.getGymName())
                .sectorName(bestRoute.getSectorName())
                .climeetDifficultyName(bestRoute.getClimeetDifficultyName())
                .gymDifficultyName(bestRoute.getGymDifficultyName())
                .gymDifficultyColor(bestRoute.getGymDifficultyColor())
                .holdColor(bestRoute.getRoute().getHoldColor())
                .build();
        }
    }
}
