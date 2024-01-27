package com.climeet.climeet_backend.domain.bestclimber.clear;

import com.climeet.climeet_backend.domain.bestclimber.clear.dto.BestClearClimberResponseDto.BestClearClimberSimpleResponse;
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
@RequestMapping("api/rank/week/climber")
public class BestClearClimberController {
    private final BestClearClimberService bestClearClimberService;

    @GetMapping("/clear")
    public ResponseEntity<List<BestClearClimberSimpleResponse>> findClimberRankingOrderClearCount(){
        return ResponseEntity.ok(bestClearClimberService.findClimberRankingOrderClearCount());
    }
}
