package com.climeet.climeet_backend.domain.routerecord;


import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto.UpdateRouteRecordDto;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordResponseDto.RouteRecordSimpleInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "RouteRecords", description = "루트 운동기록 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/route-record")
public class RouteRecordController {

    private final RouteRecordService routeRecordService;

    //생성로직은 ClimbingRecordService에서 구현

    @Operation(summary = "루트 기록 전체 조회")
    @GetMapping
    public ResponseEntity<List<RouteRecordSimpleInfo>> getRouteRecords() {
        return new ResponseEntity<>(routeRecordService.getRouteRecords(),
            HttpStatus.OK);
    }

    @Operation(summary = "루트 기록 Id 조회")
    @GetMapping("/{id}")
    public ResponseEntity<RouteRecordSimpleInfo> getRouteRecord(@PathVariable Long id) {
        return new ResponseEntity<>(routeRecordService.getRouteRecord(id),
            HttpStatus.OK);
    }

    @Operation(summary = "RouteRecord 수정")
    @PatchMapping("/{id}")
    public ResponseEntity<RouteRecordSimpleInfo> updateRouteRecord(
        @PathVariable Long id,
        @RequestBody UpdateRouteRecordDto updateRouteRecordDto) {
        return new ResponseEntity<>(routeRecordService.updateRouteRecord(id, updateRouteRecordDto),
            HttpStatus.OK);
    }

    @Operation(summary = "RouteRecord 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteRouteRecord(@PathVariable Long id){
        return new ResponseEntity<>(routeRecordService.deleteRouteRecord(id), HttpStatus.OK);
    }
}