package com.climeet.climeet_backend.domain.climbinggym.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.route.Route;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClimbingGymResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClimbingGymSimpleResponse {

        private Long id;
        private String name;

        public ClimbingGymSimpleResponse(ClimbingGym climbingGym) {
            this.id = climbingGym.getId();
            this.name = climbingGym.getName();
        }
    }


}
