package com.climeet.climeet_backend.domain.climbinggymlayoutimage.dto;

import com.climeet.climeet_backend.domain.climbinggymlayoutimage.ClimbingGymLayoutImage;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class ClimbingGymLayoutImageResponseDto {
    @Getter
    @Builder
    @AllArgsConstructor
    public static class LayoutImgListDetail {
        private Long id;
        private String imgUrl;
        private int floor;
        public static LayoutImgListDetail toDto(ClimbingGymLayoutImage climbingGymLayoutImage){
            return LayoutImgListDetail.builder()
                .id(climbingGymLayoutImage.getId())
                .imgUrl(climbingGymLayoutImage.getImgUrl())
                .floor(climbingGymLayoutImage.getFloor())
                .build();
        }
    }
}
