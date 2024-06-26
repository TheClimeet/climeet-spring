package com.climeet.climeet_backend.domain.routeversion.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

public class RouteVersionRequestDto {

    @Getter
    @NoArgsConstructor
    public static class ExistingRouteVersionData {

        private List<String> difficulty = new ArrayList<>();
        private List<Integer> layout = new ArrayList<>();
        private List<Long> sector = new ArrayList<>();
        private List<Long> route = new ArrayList<>();
    }

    @Getter
    @AllArgsConstructor
    public static class NewDifficultyData {

        private String gymDifficultyName;
        private String climeetDifficultyName;
    }

    @Getter
    @AllArgsConstructor
    public static class NewLayoutData {

        private int floor;
        private String imgUrl;
    }

    @Getter
    @AllArgsConstructor
    public static class NewSectorData {

        private String name;
        private int floor;
        private String imgUrl;
    }

    @Getter
    @AllArgsConstructor
    public static class NewRouteData {

        private String sectorName;
        private String gymDifficultyName;
        private String holdColor;
        private String imgUrl;
    }

    @Getter
    @NoArgsConstructor
    public static class NewRouteVersionData {

        private List<NewDifficultyData> difficulty = new ArrayList<>();
        private List<NewLayoutData> layout = new ArrayList<>();
        private List<NewSectorData> sector = new ArrayList<>();
        private List<NewRouteData> route = new ArrayList<>();
    }

    @Getter
    @NoArgsConstructor
    public static class CreateRouteVersionRequest {

        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate timePoint;
        private ExistingRouteVersionData existingData = new ExistingRouteVersionData();
        private NewRouteVersionData newData = new NewRouteVersionData();
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
