package com.climeet.climeet_backend.domain.route;

import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteDetailResponse;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "ClimbingRoute", description = "클라이밍 루트 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/gyms")
public class RouteController {

    private final RouteService routeService;

    @Operation(summary = "클라이밍 루트 조회 - 1601 [무빗]")
    @SwaggerApiError({ErrorStatus._EMPTY_ROUTE})
    @GetMapping("/route/{routeId}")
    public ResponseEntity<RouteDetailResponse> getRoute(@PathVariable Long routeId,
        @CurrentUser User user) {
        return ResponseEntity.ok(routeService.getRoute(routeId));
    }

    @Operation(summary = "클라이밍 암장 루트 목록 조회 - 1602 [무빗]")
    @SwaggerApiError({ErrorStatus._EMPTY_ROUTE_LIST})
    @GetMapping("/{gymId}/routes")
    public ResponseEntity<List<RouteDetailResponse>> getRouteList(@PathVariable Long gymId,
        @CurrentUser User user) {
        return ResponseEntity.ok(routeService.getRouteList(gymId));
    }
}
