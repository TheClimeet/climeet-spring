package com.climeet.climeet_backend.domain.route;

import com.climeet.climeet_backend.domain.route.dto.RouteRequestDto.RouteCreateRequestDto;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteGetResponseDto;
import com.climeet.climeet_backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "ClimbingRoute", description = "클라이밍 루트 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gym")
public class RouteController {

    private final RouteService routeService;

    @Operation(summary = "클라이밍 루트 생성")
    @PostMapping("/{gymId}/route")
    public ApiResponse<String> createRoute(@PathVariable Long gymId,
        @RequestBody RouteCreateRequestDto routeCreateRequestDto) {
        routeService.createRoute(gymId, routeCreateRequestDto);
        return ApiResponse.onSuccess("새로운 Route를 추가했습니다.");
    }

    @Operation(summary = "클라이밍 루트 조회")
    @GetMapping("/route/{routeId}")
    public ApiResponse<RouteGetResponseDto> findRouteByRouteId(@PathVariable Long routeId) {
        return ApiResponse.onSuccess(routeService.getRoute(routeId));
    }

    @Operation(summary = "클라이밍 암장 루트 목록 조회")
    @GetMapping("/{gymId}/routes")
    public ApiResponse<List<RouteGetResponseDto>> findAllRouteByGymId(@PathVariable Long gymId) {
        return ApiResponse.onSuccess(routeService.getRouteList(gymId));
    }
}
