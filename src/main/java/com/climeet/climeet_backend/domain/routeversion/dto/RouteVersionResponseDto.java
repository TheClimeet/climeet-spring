package com.climeet.climeet_backend.domain.routeversion.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingResponseDto.DifficultyMappingDetailResponse;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteDetailResponse;
import com.climeet.climeet_backend.domain.sector.dto.SectorResponseDto.SectorDetailResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RouteVersionResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RouteVersionDetailResponse {

        private Long gymId;
        private String layoutImageUrl;
        private List<SectorDetailResponse> sectorList;
        private List<RouteDetailResponse> routeList;
        private List<DifficultyMappingDetailResponse> difficultyList;

        public static RouteVersionDetailResponse toDto(ClimbingGym climbingGym,
            List<SectorDetailResponse> sectorList, List<RouteDetailResponse> routeList,
            List<DifficultyMappingDetailResponse> difficultyList, String layoutImageUrl) {
            return RouteVersionDetailResponse.builder()
                .gymId(climbingGym.getId())
                .layoutImageUrl(layoutImageUrl)
                .sectorList(sectorList)
                .routeList(routeList)
                .difficultyList(difficultyList)
                .build();
        }
    }

}
