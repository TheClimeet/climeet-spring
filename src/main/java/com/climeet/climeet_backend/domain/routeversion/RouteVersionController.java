package com.climeet.climeet_backend.domain.routeversion;

import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionRequestDto.CreateRouteVersionRequest;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionResponseDto.RouteVersionDetailResponse;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "RouteVersion", description = "암장 루트 버전 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/gym")
public class RouteVersionController {

    private final RouteVersionService routeVersionService;

    @Operation(summary = "암장의 루트 버전 일자 목록")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_VERSION_LIST})
    @GetMapping("/version/list")
    public ResponseEntity<List<LocalDate>> getRouteVersionList(
        @CurrentUser User user, @RequestParam Long gymId) {
        List<LocalDate> routeVersionList = routeVersionService.getRouteVersionList(gymId);
        return ResponseEntity.ok(routeVersionList);
    }

    @Operation(summary = "암장의 루트 버전 추가")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._DUPLICATE_ROUTE_VERSION,
        ErrorStatus._MISMATCH_ROUTE_IDS, ErrorStatus._MISMATCH_SECTOR_IDS})
    @PostMapping("/version")
    public ResponseEntity<String> createRouteVersion(
        @RequestBody CreateRouteVersionRequest createRouteVersionRequest) {
        routeVersionService.createRouteVersion(createRouteVersionRequest);
        return ResponseEntity.ok("루트 버전이 추가되었습니다.");
    }

    @Operation(summary = "암장 특정 루트 버전 데이터 불러오기")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_VERSION,
        ErrorStatus._EMPTY_ROUTE_LIST, ErrorStatus._EMPTY_SECTOR_LIST,
        ErrorStatus._MISMATCH_ROUTE_IDS, ErrorStatus._MISMATCH_SECTOR_IDS})
    @GetMapping("/version")
    public ResponseEntity<RouteVersionDetailResponse> getRouteVersionDetail(
        @CurrentUser User user, @RequestParam Long gymId,
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate timePoint) {
        return ResponseEntity.ok(routeVersionService.getRouteVersionDetail(gymId, timePoint));
    }
}
