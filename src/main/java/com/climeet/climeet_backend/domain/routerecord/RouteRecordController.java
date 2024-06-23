package com.climeet.climeet_backend.domain.routerecord;


import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto.UpdateRouteRecord;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordResponseDto.RouteRecordSimpleInfo;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "100 - RouteRecords", description = "루트 운동기록 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/route-records")
public class RouteRecordController {

    private final RouteRecordService routeRecordService;

    //생성로직은 ClimbingRecordService에서 구현

    @Operation(summary = "루트 기록 전체 조회 - 101 [훈]")
    @GetMapping
    @SwaggerApiError({ErrorStatus._EMPTY_ROUTE_RECORD})
    public ResponseEntity<List<RouteRecordSimpleInfo>> getRouteRecordList(@CurrentUser User user) {
        return ResponseEntity.ok(routeRecordService.getRouteRecordList(user));
    }

    @Operation(summary = "루트 기록 id 조회 - 102 [훈]")
    @GetMapping("/{id}")
    @SwaggerApiError({ErrorStatus._ROUTE_RECORD_NOT_FOUND, ErrorStatus._EMPTY_ROUTE,
        ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<RouteRecordSimpleInfo> getRouteRecord(
        @CurrentUser User user,
        @PathVariable Long id) {
        return ResponseEntity.ok(routeRecordService.getRouteRecord(user, id));
    }

    @Operation(summary = "RouteRecord 수정 - 104 [훈]")
    @PatchMapping("/{id}")
    @SwaggerApiError({ErrorStatus._ROUTE_RECORD_NOT_FOUND, ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<RouteRecordSimpleInfo> updateRouteRecord(
        @CurrentUser User user,
        @PathVariable Long id,
        @RequestBody UpdateRouteRecord updateRouteRecord) {
        return ResponseEntity.ok(
            routeRecordService.updateRouteRecord(user, id, updateRouteRecord));
    }

    @Operation(summary = "RouteRecord 삭제 - 103 [훈]")
    @DeleteMapping("/{id}")
    @SwaggerApiError({ErrorStatus._ROUTE_RECORD_NOT_FOUND, ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<String> deleteRouteRecord(
        @CurrentUser User user,
        @PathVariable Long id) {
        routeRecordService.deleteRouteRecord(user, id);
        return ResponseEntity.ok("루트기록이 삭제되었습니다.");
    }
}