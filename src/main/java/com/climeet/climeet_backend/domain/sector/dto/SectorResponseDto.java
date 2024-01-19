package com.climeet.climeet_backend.domain.sector.dto;

import com.climeet.climeet_backend.domain.sector.Sector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class SectorResponseDto {


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SectorDetailResponse {

        private String name;
        private int floor;

        public SectorDetailResponse(Sector sector) {
            this.name = sector.getSectorName();
            this.floor = sector.getFloor();
        }
    }


}
