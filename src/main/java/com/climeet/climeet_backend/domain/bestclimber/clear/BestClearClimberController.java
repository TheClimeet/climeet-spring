package com.climeet.climeet_backend.domain.bestclimber.clear;

import com.climeet.climeet_backend.domain.bestclimber.clear.dto.BestClearClimberResponseDto.BestClearClimberDetailInfo;
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

@Tag(name = "BestClearClimber", description = "[완등순] 금주 베스트 클라이머 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/home/rank/weeks/climbers")
public class BestClearClimberController {
    private final BestClearClimberService bestClearClimberService;

    @GetMapping("/clear")
    @Operation(summary = "701 - [훈]")
    public ResponseEntity<List<BestClearClimberDetailInfo>> getClimberRankingListOrderClearCount(
        @CurrentUser User user
    ){
        return ResponseEntity.ok(bestClearClimberService.getClimberRankingListOrderClearCount());
    }
}
