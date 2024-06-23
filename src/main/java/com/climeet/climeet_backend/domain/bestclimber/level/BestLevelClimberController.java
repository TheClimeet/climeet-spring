package com.climeet.climeet_backend.domain.bestclimber.level;

import com.climeet.climeet_backend.domain.bestclimber.level.dto.BestLevelClimberResponseDto.BestLevelClimberDetailInfo;
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

@Tag(name = "BestLevelClimber", description = "[레벨순] 금주 베스트 클라이머 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/home/rank/weeks/climbers")
public class BestLevelClimberController {
    private final BestLevelClimberService bestLevelClimberService;

    @GetMapping("/level")
    @Operation(summary = "901 [훈]")
    public ResponseEntity<List<BestLevelClimberDetailInfo>> getClimberRankingListOrderLevel(
        @CurrentUser User user
    ){
        return ResponseEntity.ok(bestLevelClimberService.getClimberRankingListOrderLevel());
    }
}
