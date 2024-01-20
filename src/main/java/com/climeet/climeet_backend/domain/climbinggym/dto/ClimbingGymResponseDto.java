package com.climeet.climeet_backend.domain.climbinggym.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.route.Route;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClimbingGymResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClimbingGymSimpleResponse {

        private Long id;
        private String name;

        public static ClimbingGymSimpleResponse toDTO(ClimbingGym climbingGym) {
            return ClimbingGymSimpleResponse.builder()
                .id(climbingGym.getId())
                .name(climbingGym.getName())
                .build();
        }
    }


}
