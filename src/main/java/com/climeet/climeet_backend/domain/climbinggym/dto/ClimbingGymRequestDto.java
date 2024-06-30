package com.climeet.climeet_backend.domain.climbinggym.dto;

import com.climeet.climeet_backend.domain.climbinggym.enums.ServiceBitmask;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClimbingGymRequestDto {

    @Getter
    @NoArgsConstructor
    public static class UpdateClimbingGymServiceRequest {

        private List<ServiceBitmask> serviceList;

    }

    @Getter
    @NoArgsConstructor
    public static class UpdateClimbingGymPriceRequest {

        private Map<String, String> priceMapList;
    }

    @Getter
    @NoArgsConstructor
    public static class ChangeClimbingGymNameRequest {
        private String name;
    }

    @Getter
    @NoArgsConstructor
    public static class ChangeClimbingGymBackgroundImageRequest {
        private String imgUrl;
    }

    @Getter
    @NoArgsConstructor
    public static class ChangeClimbingGymProfileImageRequest {
        private String imgUrl;
    }
}
