package com.climeet.climeet_backend.domain.review;

import com.climeet.climeet_backend.domain.review.dto.ReviewRequestDto.CreateReviewRequest;
import com.climeet.climeet_backend.domain.review.dto.ReviewResponseDto.ReviewDetailListResponse;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "1400 - ClimbingReview", description = "암장 리뷰 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/gyms")
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(summary = "암장 리뷰 작성 - 1401 [무빗]")
    @SwaggerApiError({ErrorStatus._RATING_OUT_OF_RANGE, ErrorStatus._EMPTY_CLIMBING_GYM,
        ErrorStatus._EMPTY_MANAGER_GYM, ErrorStatus._EMPTY_MEMBER, ErrorStatus._REVIEW_EXIST})
    @PostMapping("/{gymId}/review")
    public ResponseEntity<String> createReview(
        @RequestBody CreateReviewRequest createReviewRequest, @CurrentUser User user, @PathVariable Long gymId) {
        reviewService.createReview(createReviewRequest, user, gymId);
        return ResponseEntity.ok("리뷰를 추가했습니다.");
    }

    @Operation(summary = "특정 암장 리뷰 목록 조회 - 1402 [무빗]")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_MANAGER_GYM,
        ErrorStatus._EMPTY_MEMBER})
    @GetMapping("/{gymId}/review")
    public ResponseEntity<PageResponseDto<ReviewDetailListResponse>> getReviewList(
        @PathVariable Long gymId, @CurrentUser User user, @RequestParam int page,
        @RequestParam int size) {
        return ResponseEntity.ok(reviewService.getReviewList(gymId, user, page, size));
    }


    @Operation(summary = "암장 리뷰 수정 - 1403 [무빗]")
    @SwaggerApiError({ErrorStatus._RATING_OUT_OF_RANGE, ErrorStatus._EMPTY_CLIMBING_GYM,
        ErrorStatus._EMPTY_MANAGER_GYM, ErrorStatus._EMPTY_MEMBER, ErrorStatus._EMPTY_REVIEW})
    @PatchMapping("/{gymId}/review")
    public ResponseEntity<String> changeReview(
        @RequestBody CreateReviewRequest changeReviewRequest, @CurrentUser User user, @PathVariable Long gymId) {
        reviewService.changeReview(changeReviewRequest, user, gymId);
        return ResponseEntity.ok("리뷰를 수정했습니다.");
    }
}
