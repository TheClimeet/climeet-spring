package com.climeet.climeet_backend.domain.review.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.review.Review;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ReviewResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewSummaryDetailResponse {

        private Long gymId;
        private Float averageRating;
        private int reviewCount;
        private ReviewDetailResponse myReview;

        public static ReviewSummaryDetailResponse toDTO(ClimbingGym climbingGym,
            ReviewDetailResponse myReview) {
            return ReviewSummaryDetailResponse.builder()
                .gymId(climbingGym.getId())
                .averageRating(climbingGym.getAverageRating())
                .reviewCount(climbingGym.getReviewCount())
                .myReview(myReview)
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReviewDetailResponse {

        private Long userId;
        private String profileImageUrl;
        private String profileName;
        private Float rating;
        private String content;
        private LocalDateTime updatedAt;

        public static ReviewDetailResponse toDTO(Review review) {
            return ReviewDetailResponse.builder()
                .userId(review.getClimber().getId())
                .profileImageUrl(review.getClimber().getProfileImageUrl())
                .profileName(review.getClimber().getProfileName())
                .rating(review.getRating())
                .content(review.getContent())
                .updatedAt(review.getUpdatedAt())
                .build();

        }
    }

}
