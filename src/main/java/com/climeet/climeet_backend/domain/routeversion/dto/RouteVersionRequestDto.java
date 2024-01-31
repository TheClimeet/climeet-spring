package com.climeet.climeet_backend.domain.routeversion.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

public class RouteVersionRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateRouteVersionRequest {

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate timePoint;
        private List<Long> routeIdList;
        private List<Long> sectorIdList;
    }

    @Getter
    public static class GetFilteredRouteVersionRequest {

        private int[] floorList = null;
        private Long[] sectorIdList = null;
        private int[] gymDifficultyList = null;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate timePoint;

        public GetFilteredRouteVersionRequest() {
            this.floorList = new int[0];
            this.sectorIdList = new Long[0];
            this.gymDifficultyList = new int[0];
        }
    }

}
