package com.climeet.climeet_backend.domain.bestfollowgym;

import com.climeet.climeet_backend.domain.bestfollowgym.dto.BestFollowGymResponseDto.BestFollowGymDetailInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BestFollowGym", description = "[팔로우순] 금주 베스트 운동 기록 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/home/rank/weeks/gyms")
public class BestFollowGymController {

    private final BestFollowGymService bestFollowGymService;

    /**
     * [GET] [팔로우 순] 이번주 짐 랭킹 조회
     */
    @Operation(summary = "[팔로우 순] 이번주 짐 랭킹 조회 - 1801 [훈]")
    @GetMapping("/follow")
    public ResponseEntity<List<BestFollowGymDetailInfo>> getGymRankingListOrderFollowCount() {
        return ResponseEntity.ok(bestFollowGymService.getGymRankingListOrderFollowCount());
    }
}
