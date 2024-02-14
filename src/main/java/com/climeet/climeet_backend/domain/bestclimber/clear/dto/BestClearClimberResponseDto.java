package com.climeet.climeet_backend.domain.bestclimber.clear.dto;

import com.climeet.climeet_backend.domain.bestclimber.clear.BestClearClimber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BestClearClimberResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BestClearClimberDetailInfo{

        private Long userId;

        private int ranking;

        private String profileImageUrl;

        private String profileName;

        private int thisWeekClearCount;

        public static BestClearClimberDetailInfo toDTO(BestClearClimber bestClearClimber){
            return BestClearClimberDetailInfo.builder()
                .userId(bestClearClimber.getUserId())
                .ranking(bestClearClimber.getRanking())
                .profileImageUrl(bestClearClimber.getProfileImageUrl())
                .thisWeekClearCount(bestClearClimber.getThisWeekClearCount())
                .profileName(bestClearClimber.getProfileName())
                .build();
        }
    }
}
