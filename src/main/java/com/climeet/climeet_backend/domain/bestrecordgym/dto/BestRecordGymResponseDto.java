package com.climeet.climeet_backend.domain.bestrecordgym.dto;

import com.climeet.climeet_backend.domain.bestrecordgym.BestRecordGym;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BestRecordGymResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BestRecordGymDetailInfo {

        private Long gymId;
        private int ranking;
        private String profileImageUrl;
        private String gymName;
        private int thisWeekSelectionCount;
        private Float rating;
        private int reviewCount;

        public static BestRecordGymDetailInfo toDTO(BestRecordGym bestRecordGym) {
            return BestRecordGymDetailInfo.builder()
                .gymId(bestRecordGym.getClimbingGym().getId())
                .ranking(bestRecordGym.getRanking())
                .thisWeekSelectionCount(bestRecordGym.getThisWeekSelectionCount())
                .profileImageUrl(bestRecordGym.getProfileImageUrl())
                .gymName(bestRecordGym.getGymName())
                .rating(bestRecordGym.getRating())
                .reviewCount(bestRecordGym.getReviewCount())
                .build();
        }
    }
}

