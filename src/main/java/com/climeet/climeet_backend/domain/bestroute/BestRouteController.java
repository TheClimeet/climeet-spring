package com.climeet.climeet_backend.domain.bestroute;

import com.climeet.climeet_backend.domain.bestroute.dto.BestRouteResponseDto.BestRouteSimpleDto;
import com.climeet.climeet_backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "BestRoute", description = "[기록된 순(selected된 순)] 금주 베스트 루트 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/rank/week/routes")
public class BestRouteController {
    private final BestRouteService bestRouteService;

    @GetMapping
    public ApiResponse<List<BestRouteSimpleDto>> findRouteRankingOrderSelectionCount(){
        return ApiResponse.onSuccess(bestRouteService.findBestRouteList());
    }
}
