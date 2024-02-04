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
        private String layoutImageUrl;
    }

    @Getter
    public static class GetFilteredRouteVersionRequest {

        private int page;
        private int size;
        private int[] floorList = null;
        private Long[] sectorIdList = null;
        private int[] difficultyList = null;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate timePoint;

        public GetFilteredRouteVersionRequest() {
            this.floorList = new int[0];
            this.sectorIdList = new Long[0];
            this.difficultyList = new int[0];
            this.timePoint = LocalDate.now();
        }
    }

}
