package com.climeet.climeet_backend.domain.bestfollowgym;

import com.climeet.climeet_backend.domain.bestfollowgym.dto.BestFollowGymResponseDto.BestFollowGymSimpleDto;
import com.climeet.climeet_backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BestFollowGym", description = "[팔로우순] 금주 베스트 운동 기록 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rank/week/gym")
public class BestFollowGymController {

    private final BestFollowGymService bestFollowGymService;

    /**
     * [GET] [팔로우 순] 이번주 짐 랭킹 조회
     */
    @Operation(summary = "[팔로우 순] 이번주 짐 랭킹 조회")
    @GetMapping("/follow")
    public ApiResponse<List<BestFollowGymSimpleDto>> findGymRankingOrderFollowCount() {
        return ApiResponse.onSuccess(bestFollowGymService.findGymRankingOrderFollowCount());
    }
}
