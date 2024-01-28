package com.climeet.climeet_backend.domain.routeversion;

import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionResponseDto.RouteVersionDetailResponse;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.joda.time.LocalDate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ClimbingRoute", description = "클라이밍 루트 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/gym/version")
public class RouteVersionController {

    private final RouteVersionService routeVersionService;

    @Operation(summary = "암장의 루트 버전 일자 목록")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_VERSION_LIST})
    @GetMapping("/list")
    public ResponseEntity<List<LocalDate>> getRouteVersionList(
        @RequestParam Long gymId) {
        List<LocalDate> routeVersionList = routeVersionService.getRouteVersionList(gymId);
        return ResponseEntity.ok(routeVersionList);
    }

    @Operation(summary = "암장의 루트 버전 추가")
    @PostMapping("")
    public ResponseEntity<String> createRouteVersion(
    ) {
        return ResponseEntity.ok("루트 버전이 추가되었습니다.");
    }

    @Operation(summary = "암장 특정 루트 버전 데이터 불러오기")
    @GetMapping("")
    public ResponseEntity<RouteVersionDetailResponse> getRouteVersionDetail(
        @RequestParam Long gymId, @RequestParam LocalDate timePoint
    ) {
        return ResponseEntity.ok(routeVersionService.getRouteVersionDetail(gymId, timePoint));
    }
}
