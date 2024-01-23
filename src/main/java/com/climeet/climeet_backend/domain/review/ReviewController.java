package com.climeet.climeet_backend.domain.review;

import com.climeet.climeet_backend.domain.review.dto.ReviewRequestDto.CreateReviewRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "ClimbingReview", description = "암장 리뷰 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gym/review")
public class ReviewController {

    private final ReviewService reviewService;

    /**
     * [POST] 암장 리뷰 작성
     */
    @Operation(summary = "암장 리뷰 작성")
    @PostMapping("/")
    public ResponseEntity<String> createReview(
        @RequestBody CreateReviewRequest createReviewRequest) {
        reviewService.createReview(createReviewRequest);
        return ResponseEntity.ok("리뷰를 추가했습니다.");
    }



    @Operation(summary = "암장 리뷰 수정")
    @PatchMapping("/")
    public ResponseEntity<String> changeReview(
        @RequestBody CreateReviewRequest changeReviewRequest) {
        reviewService.changeReview(changeReviewRequest);
        return ResponseEntity.ok("리뷰를 수정했습니다.");
    }
}
