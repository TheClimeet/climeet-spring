package com.climeet.climeet_backend.domain.bestroute;

import com.climeet.climeet_backend.domain.bestroute.dto.BestRouteResponseDto.BestRouteDetailInfo;
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

@Tag(name = "BestRoute", description = "[기록된 순(selected된 순)] 금주 베스트 루트 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/home/rank/weeks/routes")
public class BestRouteController {

    private final BestRouteService bestRouteService;

    @GetMapping
    @Operation(summary = "1901 [훈]")
    public ResponseEntity<List<BestRouteDetailInfo>> getRouteRankingList(
        @CurrentUser User user
    ) {
        return ResponseEntity.ok(bestRouteService.getRouteRankingList());
    }
}
