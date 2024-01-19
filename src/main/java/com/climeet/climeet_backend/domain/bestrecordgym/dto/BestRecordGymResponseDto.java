package com.climeet.climeet_backend.domain.bestrecordgym.dto;

import com.climeet.climeet_backend.domain.bestrecordgym.BestRecordGym;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BestRecordGymResponseDto {

    @Getter
    @NoArgsConstructor
    public static class BestRecordGymSimpleDto {

        private int ranking;
        private String profileImageUrl;
        private String gymName;
        private int thisWeekSelectionCount;
        private Float rating;
        private int reviewCount;

        public BestRecordGymSimpleDto(BestRecordGym bestRecordGym) {
            this.ranking = bestRecordGym.getRanking();
            this.thisWeekSelectionCount = bestRecordGym.getThisWeekSelectionCount();
            this.profileImageUrl = bestRecordGym.getProfileImageUrl();
            this.gymName = bestRecordGym.getGymName();
            this.rating = bestRecordGym.getRating();
            this.reviewCount = bestRecordGym.getReviewCount();
        }
    }
}

