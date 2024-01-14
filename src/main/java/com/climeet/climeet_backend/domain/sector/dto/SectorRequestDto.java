package com.climeet.climeet_backend.domain.sector.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class SectorRequestDto {

    @Getter
    @NoArgsConstructor
    public static class SectorCreateRequestDto {
        private Long gymId;
        private String name;
        private int floor;
    }
}
