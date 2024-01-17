package com.climeet.climeet_backend.domain.bestfollowgym.dto;

import com.climeet.climeet_backend.domain.bestfollowgym.BestFollowGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BestFollowGymResponseDto {
    @Getter
    @NoArgsConstructor
    public static class BestFollowGymSimpleDto {

        private int ranking;
        private String profileImageUrl;
        private String gymName;
        private Long gymId;
        private int thisWeekFollowerCount;
        private Float rating;
        private int reviewCount;

        public BestFollowGymSimpleDto(BestFollowGym bestFollowGym, ClimbingGym climbingGym) {
            this.ranking = bestFollowGym.getRanking();
            this.profileImageUrl = climbingGym.getProfileImageUrl();
            this.gymName = climbingGym.getName();
            this.gymId = climbingGym.getId();
            this.thisWeekFollowerCount = bestFollowGym.getThisWeekFollowCount();
            this.rating = climbingGym.getAverageRating();
            this.reviewCount = climbingGym.getReviewCount();
        }
    }
}
