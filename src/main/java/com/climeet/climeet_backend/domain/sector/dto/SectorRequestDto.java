package com.climeet.climeet_backend.domain.sector.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class SectorRequestDto {

    @Getter
    @AllArgsConstructor
    public static class CreateSectorRequest {
        private String name;
        private int floor;
        private String imgUrl;
    }
}
