package com.climeet.climeet_backend.domain.bestrecordgym;

import com.climeet.climeet_backend.domain.bestrecordgym.dto.BestRecordGymResponseDto.BestRecordGymDetailInfo;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "600 - BestRecordGym", description = "[기록된 순(selected된 순)] 금주 베스트 운동 기록 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/home/rank/weeks/gyms")
public class BestRecordGymController {
    private final BestRecordGymService bestRecordGymService;

    @Operation(summary = "[기록된 순(selected된 순)] 이번주 짐 랭킹 조회 - 601 [훈]")
    @GetMapping("/record")
    public ResponseEntity<List<BestRecordGymDetailInfo>> getGymRankingListOrderSelectionCount(
        @CurrentUser User user
    ) {
        return ResponseEntity.ok(bestRecordGymService.getGymRankingListOrderSelectionCount());
    }

}
