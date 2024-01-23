package com.climeet.climeet_backend.domain.bestclimber.time;

import com.climeet.climeet_backend.domain.bestclimber.time.dto.BestTimeClimberResponseDto.BestTimeClimberSimpleResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BestTimeClimber", description = "[시간순] 금주 베스트 클라이머 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/rank/week/climber")
public class BestTimeClimberController {

    private final BestTimeClimberService bestTimeClimberService;

    @GetMapping("/time")
    public ResponseEntity<List<BestTimeClimberSimpleResponse>> findClimberRankingOrderLevel(){
        return ResponseEntity.ok(bestTimeClimberService.findClimberRankingOrderTime());
    }
}
