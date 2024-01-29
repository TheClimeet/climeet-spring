package com.climeet.climeet_backend.domain.sector.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class SectorRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateSectorRequest {
        private String name;
        private int floor;
    }
}
