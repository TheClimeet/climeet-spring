package com.climeet.climeet_backend.domain.route;

import com.climeet.climeet_backend.domain.route.dto.RouteRequestDto.CreateRouteRequest;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteSimpleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "ClimbingRoute", description = "클라이밍 루트 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gym")
public class RouteController {

    private final RouteService routeService;

    @Operation(summary = "클라이밍 루트 생성")
    @PostMapping("/route")
    public ResponseEntity<String> createRoute(
        @RequestPart(value = "image") MultipartFile routeImage,
        @RequestPart CreateRouteRequest createRouteRequest
    ) {
        routeService.createRoute(createRouteRequest, routeImage);
        return ResponseEntity.ok("루트를 추가했습니다.");
    }

    @Operation(summary = "클라이밍 루트 조회")
    @GetMapping("/route/{routeId}")
    public ResponseEntity<RouteSimpleResponse> getRoute(@PathVariable Long routeId) {
        return ResponseEntity.ok(routeService.getRoute(routeId));
    }

    @Operation(summary = "클라이밍 암장 루트 목록 조회")
    @GetMapping("/{gymId}/routes")
    public ResponseEntity<List<RouteSimpleResponse>> getRouteList(@PathVariable Long gymId) {
        return ResponseEntity.ok(routeService.getRouteList(gymId));
    }
}
