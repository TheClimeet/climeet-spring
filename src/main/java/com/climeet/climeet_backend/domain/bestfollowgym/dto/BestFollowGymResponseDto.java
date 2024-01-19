package com.climeet.climeet_backend.domain.bestfollowgym.dto;

import com.climeet.climeet_backend.domain.bestfollowgym.BestFollowGym;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BestFollowGymResponseDto {

    @Getter
    @NoArgsConstructor
    public static class BestFollowGymSimpleDto {

        private int ranking;
        private String profileImageUrl;
        private String gymName;
        private int thisWeekFollowerCount;
        private Float rating;
        private int reviewCount;

        public BestFollowGymSimpleDto(BestFollowGym bestFollowGym) {
            this.ranking = bestFollowGym.getRanking();
            this.thisWeekFollowerCount = bestFollowGym.getThisWeekFollowCount();
            this.profileImageUrl = bestFollowGym.getProfileImageUrl();
            this.gymName = bestFollowGym.getGymName();
            this.rating = bestFollowGym.getRating();
            this.reviewCount = bestFollowGym.getReviewCount();
        }
    }
}
