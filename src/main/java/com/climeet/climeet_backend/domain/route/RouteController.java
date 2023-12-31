package com.climeet.climeet_backend.domain.route;

import com.climeet.climeet_backend.domain.route.dto.RouteRequestDto.RouteCreateRequestDto;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteGetResponseDto;
import com.climeet.climeet_backend.global.response.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    // 암장 루트 추가
    @PostMapping("/gym/{gymId}/route")
    public ApiResponse<String> createRoute(@PathVariable Long gymId,
        @RequestBody RouteCreateRequestDto routeCreateRequestDto) {
        routeService.createRoute(
            gymId,
            routeCreateRequestDto.getSectorId(),
            routeCreateRequestDto.getName(),
            routeCreateRequestDto.getDifficulty(),
            routeCreateRequestDto.getRouteImageUrl());
        return ApiResponse.onSuccess("새로운 Route를 추가했습니다.");
    }

    // 암장 루트 정보 조회
    @GetMapping("/gym/route/{routeId}")
    public ApiResponse<RouteGetResponseDto> findRouteByRouteId(@PathVariable Long routeId) {
        return ApiResponse.onSuccess(routeService.getRoute(routeId));
    }

    // 암장 루트 리스트 정보 조회
    @GetMapping("/gym/{gymId}/routes")
    public ApiResponse<List<RouteGetResponseDto>> findAllRouteByGymId(@PathVariable Long gymId) {
        return ApiResponse.onSuccess(routeService.getRouteList(gymId));
    }
}
