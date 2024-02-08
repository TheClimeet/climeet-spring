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
    public static class BestLevelClimberDetailInfo{

        private int ranking;

        private String profileImageUrl;

        private String profileName;

        private int thisWeekHighDifficulty;

        private Long highDifficultyCount;

        public static BestLevelClimberDetailInfo toDTO(
            BestLevelClimber bestLevelClimber){
            return BestLevelClimberDetailInfo.builder()
                .ranking(bestLevelClimber.getRanking())
                .profileImageUrl(bestLevelClimber.getProfileImageUrl())
                .thisWeekHighDifficulty(bestLevelClimber.getDifficulty())
                .profileName(bestLevelClimber.getProfileName())
                .highDifficultyCount(bestLevelClimber.getDifficultyCount())
                .build();
        }
    }
}
