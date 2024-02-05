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
        private Integer floor;
        private Long sectorId;
        private Integer difficulty;

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate timePoint;

        public GetFilteredRouteVersionRequest() {
            this.floor = null;
            this.sectorId = null;
            this.difficulty = null;
            this.timePoint = LocalDate.now();
        }
    }

}
