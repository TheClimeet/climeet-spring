package com.climeet.climeet_backend.domain.review;

import com.climeet.climeet_backend.domain.climber.Climber;
import com.climeet.climeet_backend.domain.climber.ClimberRepository;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.review.dto.ReviewRequestDto.CreateReviewRequest;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClimbingGymRepository climbingGymRepository;
    private final ClimberRepository climberRepository;

    @Transactional
    public void createReview(CreateReviewRequest createReviewRequest, User user) {
        // 리뷰 내용 길이 초과 확인
        if (createReviewRequest.getContent().length() > 1000) {
            throw new GeneralException(ErrorStatus._CONTENT_TOO_LARGE);
        }

        // 리뷰 rating의 범위 확인
        if (createReviewRequest.getRating() < 0 || createReviewRequest.getRating() > 5
            || createReviewRequest.getRating() % 0.5 != 0) {
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


    @Transactional
    public void changeReview(CreateReviewRequest changeReviewRequest, User user) {
        // 리뷰 rating의 범위 확인
        if (changeReviewRequest.getRating() < 0 || changeReviewRequest.getRating() > 5
            || changeReviewRequest.getRating() % 0.5 != 0) {
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

        review.setRating(changeReviewRequest.getRating());
        review.setContent(changeReviewRequest.getContent());

        reviewRepository.save(review);
    }

}