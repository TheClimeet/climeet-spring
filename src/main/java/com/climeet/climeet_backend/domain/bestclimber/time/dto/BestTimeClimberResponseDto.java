package com.climeet.climeet_backend.domain.bestclimber.time.dto;

import static com.climeet.climeet_backend.global.utils.DateTimeConverter.convertDoubleToStringTime;

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
    public static class BestTimeClimberDetailInfo{

        private Long userId;

        private int ranking;

        private String profileImageUrl;

        private String profileName;

        private String thisWeekTotalClimbingTime;

        public static BestTimeClimberDetailInfo toDTO(
            BestTimeClimber bestTimeClimber){
            return BestTimeClimberDetailInfo.builder()
                .userId(bestTimeClimber.getUserId())
                .ranking(bestTimeClimber.getRanking())
                .profileImageUrl(bestTimeClimber.getProfileImageUrl())
                .thisWeekTotalClimbingTime(convertDoubleToStringTime(bestTimeClimber.getThisWeekTotalClimbingTime()))
                .profileName(bestTimeClimber.getProfileName())
                .build();
        }
    }
}
