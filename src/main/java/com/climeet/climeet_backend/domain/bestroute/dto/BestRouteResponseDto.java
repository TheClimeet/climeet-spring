package com.climeet.climeet_backend.domain.bestroute.dto;

import com.climeet.climeet_backend.domain.bestroute.BestRoute;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BestRouteResponseDto {

    @Getter
    @NoArgsConstructor
    public static class BestRouteSimpleDto {

        private int ranking;
        private String routeImageUrl;
        private int thisWeekSelectionCount;
        private String gymName;
        private String sectorName;
        private int level;

        public BestRouteSimpleDto(BestRoute bestRoute) {
            this.ranking = bestRoute.getRanking();
            this.thisWeekSelectionCount = bestRoute.getThisWeekSelectionCount();
            this.routeImageUrl = bestRoute.getRouteImageUrl();
            this.gymName = bestRoute.getGymName();
            this.sectorName = bestRoute.getSectorName();
            this.level = bestRoute.getLevel();
        }
    }
}
