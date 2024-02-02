package com.climeet.climeet_backend.domain.climbinggym.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClimbingGymRequestDto {

    @Getter
    @NoArgsConstructor
    public static class UpdateClimbingGymInfoRequest {
        private Long gymId;
    }

}
