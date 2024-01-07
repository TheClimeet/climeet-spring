package com.climeet.climeet_backend.domain.routerecord;


import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordSimpleInfo;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordResponseDto.RouteRecordSimpleInfo;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RequiredArgsConstructor
@RestController
@RequestMapping("/route-record")
public class RouteRecordController {
    private final RouteRecordService routeRecordService;

    //생성로직은 ClimbingRecordService에서 구현

    /**
     * 간편기록 총 조회
     * @param
     * @return List<RouteRecordSimpleInfo>
     */
    @Operation(summary = "루트 기록 전체 조회", description = "루트 간편 기록 전체 조회")
    @GetMapping
    public ResponseEntity<List<RouteRecordSimpleInfo>> getRouteRecords() {
        return new ResponseEntity<>(routeRecordService.getRouteRecords(),
            HttpStatus.OK);
    }

    /**
     * Id로 조회
     * @param
     * @return RouteRecordSimpleInfo
     */
    @Operation(summary = "루트 기록 Id 조회", description = "루트 간편 기록 id 조회")
    @GetMapping("/{id}")
        public ResponseEntity<RouteRecordSimpleInfo> getRouteRecord(@PathVariable Long id) {
        return new ResponseEntity<>(routeRecordService.getRouteRecord(id),
            HttpStatus.OK);
    }


}