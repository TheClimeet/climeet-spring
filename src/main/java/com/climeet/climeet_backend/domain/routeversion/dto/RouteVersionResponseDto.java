package com.climeet.climeet_backend.domain.routeversion.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggymlayoutimage.ClimbingGymLayoutImage;
import com.climeet.climeet_backend.domain.climbinggymlayoutimage.dto.ClimbingGymLayoutImageResponseDto.LayoutImgListDetail;
import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingResponseDto.DifficultyMappingDetailResponse;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteDetailResponse;
import com.climeet.climeet_backend.domain.routeversion.RouteVersion;
import com.climeet.climeet_backend.domain.sector.dto.SectorResponseDto.SectorDetailResponse;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class RouteVersionResponseDto {


    @Builder
    @Getter
    @AllArgsConstructor
    public static class RouteVersionFilteringKeyResponse {

        private Long gymId;
        private LocalDate timePoint;
        private List<LayoutImgListDetail> layoutList;
        private List<SectorDetailResponse> sectorList;
        private List<DifficultyMappingDetailResponse> difficultyList;
        private int maxFloor;

        public static RouteVersionFilteringKeyResponse toDTO(ClimbingGym climbingGym,
            List<SectorDetailResponse> sectorList,
            List<DifficultyMappingDetailResponse> difficultyList,
            List<LayoutImgListDetail> layoutImageList, int maxFloor,
            RouteVersion routeVersion) {
            return RouteVersionFilteringKeyResponse.builder()
                .gymId(climbingGym.getId())
                .timePoint(routeVersion.getTimePoint())
                .layoutList(layoutImageList)
                .sectorList(sectorList)
                .difficultyList(difficultyList)
                .maxFloor(maxFloor)
                .build();
        }
    }

    @Builder
    @Getter
    @AllArgsConstructor
    public static class RouteVersionAllDataResponse {

        private Long gymId;
        private LocalDate timePoint;
        private int maxFloor;
        private List<DifficultyMappingDetailResponse> difficultyList;
        private List<LayoutImgListDetail> layoutList;
        private List<SectorDetailResponse> sectorList;
        private List<RouteDetailResponse> routeList;

        public static RouteVersionAllDataResponse toDTO(ClimbingGym climbingGym,
            int maxFloor, RouteVersion routeVersion,
            List<DifficultyMappingDetailResponse> difficultyList,
            List<LayoutImgListDetail> layoutImageList,
            List<SectorDetailResponse> sectorList,
            List<RouteDetailResponse> routeList) {
            return RouteVersionAllDataResponse.builder()
                .gymId(climbingGym.getId())
                .timePoint(routeVersion.getTimePoint())
                .maxFloor(maxFloor)
                .difficultyList(difficultyList)
                .layoutList(layoutImageList)
                .sectorList(sectorList)
                .routeList(routeList)
                .build();
        }
    }

}
