package com.climeet.climeet_backend.domain.bestclimber.level;

import com.climeet.climeet_backend.domain.bestclimber.level.dto.BestLevelClimberResponseDto.BestLevelClimberSimpleResponse;
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
@RequestMapping("/rank/week/climber")
public class BestLevelClimberController {
    private final BestLevelClimberService bestLevelClimberService;

    @GetMapping("/level")
    public ResponseEntity<List<BestLevelClimberSimpleResponse>> findClimberRankingOrderLevel(){
        return ResponseEntity.ok(bestLevelClimberService.findClimberRankingOrderLevel());
    }
}
