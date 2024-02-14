package com.climeet.climeet_backend.domain.bestfollowgym.dto;

import com.climeet.climeet_backend.domain.bestfollowgym.BestFollowGym;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BestFollowGymResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BestFollowGymDetailInfo {

        private Long gymId;
        private int ranking;
        private String profileImageUrl;
        private String gymName;
        private int thisWeekFollowerCount;
        private Float rating;
        private int reviewCount;

        public static BestFollowGymDetailInfo toDTO(BestFollowGym bestFollowGym) {
            return BestFollowGymDetailInfo.builder()
                .gymId(bestFollowGym.getClimbingGym().getId())
                .ranking(bestFollowGym.getRanking())
                .thisWeekFollowerCount(bestFollowGym.getThisWeekFollowCount())
                .profileImageUrl(bestFollowGym.getProfileImageUrl())
                .gymName(bestFollowGym.getGymName())
                .rating(bestFollowGym.getRating())
                .reviewCount(bestFollowGym.getReviewCount())
                .build();
        }
    }
}
