package com.climeet.climeet_backend.domain.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateReviewRequest {

        private Long climbingGymId;
        private Long climberId;
        private String content;
        private Float rating; // (0 <= rating <= 5) 의 실수(0.5 단위)
    }

}
