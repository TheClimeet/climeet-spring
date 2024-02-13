package com.climeet.climeet_backend.domain.climbinggym.dto;

import com.climeet.climeet_backend.domain.climbinggym.enums.ServiceBitmask;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClimbingGymRequestDto {

    @Getter
    @NoArgsConstructor
    public static class UpdateClimbingGymServiceRequest {

        private List<ServiceBitmask> serviceList;

    }

}
