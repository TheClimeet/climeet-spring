package com.climeet.climeet_backend.domain.routeversion.dto;

import com.climeet.climeet_backend.domain.climbinggymlayoutimage.dto.ClimbingGymLayoutImageRequestDto.CreateLayoutImageRequest;
import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingRequestDto.CreateDifficultyMappingRequest;
import com.climeet.climeet_backend.domain.route.dto.RouteRequestDto.CreateRouteRequest;
import com.climeet.climeet_backend.domain.sector.dto.SectorRequestDto.CreateSectorRequest;
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
    @NoArgsConstructor
    public static class NewRouteVersionData {

        private List<CreateDifficultyMappingRequest> difficulty = new ArrayList<>();
        private List<CreateLayoutImageRequest> layout = new ArrayList<>();
        private List<CreateSectorRequest> sector = new ArrayList<>();
        private List<CreateRouteRequest> route = new ArrayList<>();

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
