package com.climeet.climeet_backend.domain.review.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class ReviewRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateReviewRequest {

        private Long climbingGymId;
        private String content;
        private Float rating;
    }

}
