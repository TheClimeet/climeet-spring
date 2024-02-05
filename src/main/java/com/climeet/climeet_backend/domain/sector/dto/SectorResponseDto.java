package com.climeet.climeet_backend.domain.sector.dto;

import com.climeet.climeet_backend.domain.sector.Sector;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class SectorResponseDto {


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectorDetailResponse {

        private Long sectorId;
        private String name;
        private int floor;
        private String sectorImageUrl;

        public static SectorDetailResponse toDto(Sector sector) {
            return SectorDetailResponse.builder()
                .sectorId(sector.getId())
                .name(sector.getSectorName())
                .floor(sector.getFloor())
                .sectorImageUrl(sector.getSectorImageUrl())
                .build();

        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectorSimpleResponse {

        private Long sectorId;

        public static SectorSimpleResponse toDto(Sector sector) {
            return SectorSimpleResponse.builder()
                .sectorId(sector.getId())
                .build();
        }
    }


}
