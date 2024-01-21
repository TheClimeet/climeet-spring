package com.climeet.climeet_backend.domain.review;

import com.climeet.climeet_backend.domain.climber.Climber;
import com.climeet.climeet_backend.domain.climber.ClimberRepository;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.review.dto.ReviewRequestDto.CreateReviewRequest;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.route.dto.RouteRequestDto.CreateRouteRequest;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ClimbingGymRepository climbingGymRepository;
    private final ClimberRepository climberRepository;

    @Transactional
    public void createReview(CreateReviewRequest createReviewRequest) {
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
        if(climbingGym.getManager() == null){
            throw new GeneralException(ErrorStatus._UNREGISTERED_GYM);
        }

        Climber climber = climberRepository.findById(createReviewRequest.getClimberId())
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

}