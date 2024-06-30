package com.climeet.climeet_backend.domain.climbinggymlayoutimage.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class ClimbingGymLayoutImageRequestDto {
    @Getter
    @AllArgsConstructor
    public static class CreateLayoutImageRequest {

        private int floor;
        private String imgUrl;
    }

}
