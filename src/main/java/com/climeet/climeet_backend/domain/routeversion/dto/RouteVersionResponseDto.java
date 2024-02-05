package com.climeet.climeet_backend.domain.routeversion.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingResponseDto.DifficultyMappingDetailResponse;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteDetailResponse;
import com.climeet.climeet_backend.domain.routeversion.RouteVersion;
import com.climeet.climeet_backend.domain.sector.dto.SectorResponseDto.SectorDetailResponse;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import java.time.LocalDate;
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
    public static class RouteVersionFilteringKeyResponse {

        private Long gymId;
        private LocalDate timePoint;
        private String layoutImageUrl;
        private List<SectorDetailResponse> sectorList;
        private List<DifficultyMappingDetailResponse> difficultyList;

        public static RouteVersionFilteringKeyResponse toDto(ClimbingGym climbingGym,
            List<SectorDetailResponse> sectorList,
            List<DifficultyMappingDetailResponse> difficultyList, RouteVersion routeVersion) {
            return RouteVersionFilteringKeyResponse.builder()
                .gymId(climbingGym.getId())
                .timePoint(routeVersion.getTimePoint())
                .layoutImageUrl(routeVersion.getLayoutImageUrl())
                .sectorList(sectorList)
                .difficultyList(difficultyList)
                .build();
        }
    }

}
