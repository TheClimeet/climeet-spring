package com.climeet.climeet_backend.domain.review;

import com.climeet.climeet_backend.domain.climber.Climber;
import com.climeet.climeet_backend.domain.climber.ClimberRepository;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.review.dto.ReviewRequestDto.CreateReviewRequest;
import com.climeet.climeet_backend.domain.review.dto.ReviewResponseDto.ReviewDetailListResponse;
import com.climeet.climeet_backend.domain.review.dto.ReviewResponseDto.ReviewDetailResponse;
import com.climeet.climeet_backend.domain.review.dto.ReviewResponseDto.ReviewSummaryResponse;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClimbingGymRepository climbingGymRepository;
    private final ClimberRepository climberRepository;

    private static final float MIN_RATING = 0;
    private static final float MAX_RATING = 5;
    private static final float STEP_RATING = 0.5f;


    private static Boolean IsInvalidRating(Float rating) {
        if (rating < MIN_RATING || rating > MAX_RATING || rating % STEP_RATING != 0) {
            return true;
        }
        return false;
    }


    @Transactional
    public void createReview(CreateReviewRequest createReviewRequest, User user) {

        // 리뷰 rating의 범위 확인
        if (IsInvalidRating(createReviewRequest.getRating())) {
            throw new GeneralException(ErrorStatus._RATING_OUT_OF_RANGE);
        }

        ClimbingGym climbingGym = climbingGymRepository.findById(
                createReviewRequest.getClimbingGymId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        // 관리자가 등록된 암장인지 확인
        if (climbingGym.getManager() == null) {
            throw new GeneralException(ErrorStatus._EMPTY_MANAGER_GYM);
        }

        Climber climber = climberRepository.findById(user.getId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MEMBER));

        // 이미 작성된 리뷰가 있는지 확인 (1명의 유저는 1개의 암장에 1개의 리뷰만 가능)
        Optional<Review> optionalReview = reviewRepository.findByClimbingGymAndClimber(
            climbingGym, climber);
        if (optionalReview.isPresent()) {
            throw new GeneralException(ErrorStatus._REVIEW_EXIST);
        }

        // 리뷰 추가로 인한 평균 리뷰 갱신
        climbingGym.reviewCreate(createReviewRequest.getRating());

        reviewRepository.save(Review.toEntity(createReviewRequest, climbingGym, climber));
    }

    public PageResponseDto<ReviewDetailListResponse> getReviewList(Long gymId, User user,
        int page, int size) {

        ClimbingGym climbingGym = climbingGymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        // 관리자가 등록된 암장인지 확인
        if (climbingGym.getManager() == null) {
            throw new GeneralException(ErrorStatus._EMPTY_MANAGER_GYM);
        }

        Climber climber = climberRepository.findById(user.getId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MEMBER));

        ReviewSummaryResponse reviewSummaryResponse = null;
        if (page == 0) {
            // 사용자 리뷰가 있으면 가져오고, 없으면 null을 myReview에 넣음
            Optional<Review> optionalReview = reviewRepository.findByClimbingGymAndClimber(
                climbingGym, climber);
            ReviewDetailResponse myReview = null;
            if (optionalReview.isPresent()) {
                myReview = ReviewDetailResponse.toDTO(optionalReview.get());
            }

            reviewSummaryResponse = ReviewSummaryResponse.toDTO(climbingGym, myReview);
        }

        Pageable pageable = PageRequest.of(page, size);

        Slice<Review> reviewSlice = reviewRepository.findByClimbingGymAndClimberIsNotOrderByUpdatedAtDesc(
            climbingGym,
            climber, pageable);

        List<ReviewDetailResponse> reviewList = reviewSlice.stream()
            .map(review -> ReviewDetailResponse.toDTO(review))
            .toList();

        ReviewDetailListResponse reviewDetailListResponse = ReviewDetailListResponse.toDto(
            reviewSummaryResponse, reviewList);

        return new PageResponseDto<>(pageable.getPageNumber(), reviewSlice.hasNext(),
            reviewDetailListResponse);
    }


    @Transactional
    public void changeReview(CreateReviewRequest changeReviewRequest, User user) {
        // 리뷰 rating의 범위 확인
        if (IsInvalidRating(changeReviewRequest.getRating())) {
            throw new GeneralException(ErrorStatus._RATING_OUT_OF_RANGE);
        }

        ClimbingGym climbingGym = climbingGymRepository.findById(
                changeReviewRequest.getClimbingGymId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        // 관리자가 등록된 암장인지 확인
        if (climbingGym.getManager() == null) {
            throw new GeneralException(ErrorStatus._EMPTY_MANAGER_GYM);
        }

        Climber climber = climberRepository.findById(user.getId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MEMBER));

        // 리뷰 데이터 불러오기
        Review review = reviewRepository.findByClimbingGymAndClimber(
                climbingGym, climber)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_REVIEW));

        // 리뷰 추가로 인한 평균 리뷰 갱신
        climbingGym.reviewChange(review.getRating(), changeReviewRequest.getRating());

        review.changeReview(changeReviewRequest.getRating(), changeReviewRequest.getContent());

        reviewRepository.save(review);
    }

}