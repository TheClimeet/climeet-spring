package com.climeet.climeet_backend.domain.review;

import com.climeet.climeet_backend.domain.review.dto.ReviewRequestDto.CreateReviewRequest;
import com.climeet.climeet_backend.domain.review.dto.ReviewResponseDto.ReviewDetailResponse;
import com.climeet.climeet_backend.domain.review.dto.ReviewResponseDto.ReviewSummaryDetailResponse;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "ClimbingReview", description = "암장 리뷰 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gym")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "암장 리뷰 작성")
    @SwaggerApiError({ErrorStatus._CONTENT_TOO_LARGE, ErrorStatus._RATING_OUT_OF_RANGE,
        ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_MANAGER_GYM, ErrorStatus._EMPTY_MEMBER,
        ErrorStatus._REVIEW_EXIST})
    @PostMapping("/review")
    public ResponseEntity<String> createReview(
        @RequestBody CreateReviewRequest createReviewRequest) {
        reviewService.createReview(createReviewRequest);
        return ResponseEntity.ok("리뷰를 추가했습니다.");
    }

    @Operation(summary = "특정 암장 리뷰 요약 조회(평균 별점, 리뷰 개수, 본인 리뷰")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_MANAGER_GYM,
        ErrorStatus._EMPTY_MEMBER})
    @GetMapping("/{gymId}/review/summary")
    public ResponseEntity<ReviewSummaryDetailResponse> getReviewSummary(
        @PathVariable Long gymId, @RequestParam Long userId) {
        return ResponseEntity.ok(reviewService.getReviewSummary(gymId, userId));
    }

    @Operation(summary = "특정 암장 리뷰 목록 조회")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_MANAGER_GYM,
        ErrorStatus._EMPTY_MEMBER})
    @GetMapping("/{gymId}/review")
    public ResponseEntity<PageResponseDto<List<ReviewDetailResponse>>> getReviewList(
        @PathVariable Long gymId, @RequestParam Long userId, @RequestParam int page,
        @RequestParam int size) {
        return ResponseEntity.ok(reviewService.getReviewList(gymId, userId, page, size));
    }
}
