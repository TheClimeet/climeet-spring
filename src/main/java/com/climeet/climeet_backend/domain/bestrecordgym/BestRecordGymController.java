package com.climeet.climeet_backend.domain.bestrecordgym;

import com.climeet.climeet_backend.domain.bestfollowgym.BestFollowGymService;
import com.climeet.climeet_backend.domain.bestfollowgym.dto.BestFollowGymResponseDto.BestFollowGymSimpleDto;
import com.climeet.climeet_backend.domain.bestrecordgym.dto.BestRecordGymResponseDto.BestRecordGymSimpleDto;
import com.climeet.climeet_backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BestRecordGym", description = "[기록된 순(selected된 순)] 금주 베스트 운동 기록 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rank/week/gym")
public class BestRecordGymController {
    private final BestRecordGymService bestRecordGymService;

    /**
     * [GET] [기록된 순(selected된 순)] 이번주 짐 랭킹 조회
     */
    @Operation(summary = "[기록된 순(selected된 순)] 이번주 짐 랭킹 조회")
    @GetMapping("/record")
    public ApiResponse<List<BestRecordGymSimpleDto>> findGymRankingOrderSelectionCount() {
        return ApiResponse.onSuccess(bestRecordGymService.findGymRankingOrderSelectionCount());
    }

}