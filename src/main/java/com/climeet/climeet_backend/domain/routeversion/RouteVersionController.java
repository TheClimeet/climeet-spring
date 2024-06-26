package com.climeet.climeet_backend.domain.routeversion;

import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteDetailResponse;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionRequestDto.CreateRouteVersionRequest;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionRequestDto.GetFilteredRouteVersionRequest;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionResponseDto.RouteVersionAllDataResponse;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionResponseDto.RouteVersionFilteringKeyResponse;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.common.PageResponseDto;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "RouteVersion", description = "암장 루트 버전 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/gyms")
public class RouteVersionController {

    private final RouteVersionService routeVersionService;

    @Operation(summary = "암장의 루트 버전 일자 목록 - 1102 [무빗]")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_VERSION_LIST})
    @GetMapping("/{gymId}/version/list")
    public ResponseEntity<List<LocalDate>> getRouteVersionList(@CurrentUser User user,
        @PathVariable Long gymId) {
        List<LocalDate> routeVersionList = routeVersionService.getRouteVersionList(gymId);
        return ResponseEntity.ok(routeVersionList);
    }

    @Operation(summary = "암장의 루트 버전 추가 - 1104 [무빗]")
    @SwaggerApiError({ErrorStatus._EMPTY_MANAGER, ErrorStatus._MISMATCH_SECTOR_DATA,
        ErrorStatus._MISMATCH_DIFFICULTY_DATA})
    @PostMapping("/version")
    public ResponseEntity<String> createRouteVersion(
        @RequestBody CreateRouteVersionRequest createRouteVersionRequest,
        @CurrentUser User user) {
        routeVersionService.createRouteVersion(createRouteVersionRequest, user);
        return ResponseEntity.ok("루트 버전이 추가되었습니다.");
    }

    @Operation(summary = "암장 특정 루트버전 필터링 키 불러오기 - 1101 [무빗]", description = "timePoint 값은 비필수 값이며, 입력되지 않았을 때의 default는 오늘 날짜입니다.")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_VERSION,
        ErrorStatus._MISMATCH_SECTOR_IDS})
    @GetMapping("/{gymId}/version/key")
    public ResponseEntity<RouteVersionFilteringKeyResponse> getRouteVersionFilteringKey(
        @CurrentUser User user, @PathVariable Long gymId,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate timePoint) {
        return ResponseEntity.ok(
            routeVersionService.getRouteVersionFilteringKey(gymId, timePoint));
    }

    @Operation(summary = "암장 특정 루트버전 루트 리스트 불러오기 (필터링 포함) - 1103 [무빗]", description = "timePoint 값은 비필수 값이며, 입력되지 않았을 때의 default는 오늘 날짜입니다. \n 각 List 필터링 값 또한 비필수이며, 만약 특정 List에 필터링될 값이 없다면 입력하지 않아도 문제 없습니다.")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._MISMATCH_ROUTE_IDS})
    @PostMapping("/{gymId}/version/route")
    public ResponseEntity<PageResponseDto<List<RouteDetailResponse>>> getRouteVersionFiltering(
        @CurrentUser User user, @PathVariable Long gymId,
        @RequestBody GetFilteredRouteVersionRequest getFilteredRouteVersionRequest) {
        return ResponseEntity.ok(
            routeVersionService.getRouteVersionFilteringRouteList(gymId,
                getFilteredRouteVersionRequest));
    }

    @Operation(summary = "암장 특정 루트버전의 모든 데이터 불러오기 (루트버전 수정용) - 1105 [무빗]")
    @SwaggerApiError({ErrorStatus._EMPTY_MANAGER, ErrorStatus._EMPTY_VERSION})
    @GetMapping("/{gymId}/version/all")
    public ResponseEntity<RouteVersionAllDataResponse> getRouteVersionAllData(
        @CurrentUser User user, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate timePoint) {
        return ResponseEntity.ok(
            routeVersionService.getRouteVersionAllData(user, timePoint));
    }
}
