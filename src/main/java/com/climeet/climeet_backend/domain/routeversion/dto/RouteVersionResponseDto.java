package com.climeet.climeet_backend.domain.routeversion.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingResponseDto.DifficultyMappingDetailResponse;
import com.climeet.climeet_backend.domain.routeversion.RouteVersion;
import com.climeet.climeet_backend.domain.sector.dto.SectorResponseDto.SectorDetailResponse;
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
        private Long layoutImageUrlId;
        private String layoutImageUrl;
        private List<SectorDetailResponse> sectorList;
        private List<DifficultyMappingDetailResponse> difficultyList;
        private List<Integer> floorList;

        public static RouteVersionFilteringKeyResponse toDto(ClimbingGym climbingGym,
            List<SectorDetailResponse> sectorList,
            List<DifficultyMappingDetailResponse> difficultyList, List<Integer> floorList,
            RouteVersion routeVersion) {
            return RouteVersionFilteringKeyResponse.builder()
                .gymId(climbingGym.getId())
                .timePoint(routeVersion.getTimePoint())
                .layoutImageUrlId(routeVersion.getClimbingGymLayoutImage().getId())
                .layoutImageUrl(routeVersion.getClimbingGymLayoutImage().getImgUrl())
                .sectorList(sectorList)
                .difficultyList(difficultyList)
                .floorList(floorList)
                .build();
        }
    }

}
