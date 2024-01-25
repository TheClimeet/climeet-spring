package com.climeet.climeet_backend.domain.bestclimber.time.dto;

import com.climeet.climeet_backend.domain.bestclimber.time.BestTimeClimber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BestTimeClimberResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BestTimeClimberSimpleResponse{

        private int ranking;

        private String profileImageUrl;

        private String profileName;

        private Long thisWeekTotalClimbingTime;

        public static BestTimeClimberSimpleResponse toDTO(
            BestTimeClimber bestTimeClimber){
            return BestTimeClimberSimpleResponse.builder()
                .ranking(bestTimeClimber.getRanking())
                .profileImageUrl(bestTimeClimber.getProfileImageUrl())
                .thisWeekTotalClimbingTime(bestTimeClimber.getThisWeekTotalClimbingTime())
                .profileName(bestTimeClimber.getProfileName())
                .build();
        }
    }
}
