package com.climeet.climeet_backend.domain.bestclimber.level.dto;

import com.climeet.climeet_backend.domain.bestclimber.level.BestLevelClimber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BestLevelClimberResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BestLevelClimberSimpleResponse{

        private int ranking;

        private String profileImageUrl;

        private String profileName;

        private int thisWeekHighDifficulty;

        public static BestLevelClimberSimpleResponse toDTO(
            BestLevelClimber bestLevelClimber){
            return BestLevelClimberSimpleResponse.builder()
                .ranking(bestLevelClimber.getRanking())
                .profileImageUrl(bestLevelClimber.getProfileImageUrl())
                .thisWeekHighDifficulty(bestLevelClimber.getThisWeekHighDifficulty())
                .profileName(bestLevelClimber.getProfileName())
                .build();
        }
    }
}
