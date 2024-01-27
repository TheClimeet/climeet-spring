package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.UpdateClimbingRecordDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.CreateClimbingRecordDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordDetailInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordStatisticsInfo;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ClimbingRecords", description = "클라이밍 운동기록 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/climbing-record")
public class ClimbingRecordController {

    private final ClimbingRecordService climbingRecordService;


    @Operation(summary = "클라이밍 기록 생성")
    @PostMapping
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_ROUTE})
    public ResponseEntity<String> addClimbingRecord(
        @CurrentUser User user,
        @RequestBody CreateClimbingRecordDto requestDto) {
        climbingRecordService.createClimbingRecord(user, requestDto);
        return ResponseEntity.ok("클라이밍 기록을 생성하였습니다.");
    }

    @Operation(summary = "클라이밍 간편 기록 전체 조회")
    @GetMapping
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_RECORD})
    public ResponseEntity<List<ClimbingRecordSimpleInfo>> getClimbingRecords() {
        return ResponseEntity.ok(climbingRecordService.getClimbingRecords());
    }


    @Operation(summary = "나의 클라이밍 기록 날짜 조회")
    @GetMapping("/between-dates")
    @SwaggerApiError({ErrorStatus._INVALID_DATE_RANGE, ErrorStatus._EMPTY_CLIMBING_RECORD, ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<List<ClimbingRecordSimpleInfo>> getClimbingRecordsBetweenDates(
        @CurrentUser User user,
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ClimbingRecordSimpleInfo> climbingRecords
            = climbingRecordService.getClimbingRecordsBetweenLocalDates(user, startDate, endDate);
        return ResponseEntity.ok(climbingRecords);
    }

    @Operation(summary = "클라이밍 기록 id 조회 (루트기록들 포함. 단, 루트 기록은 없어도 예외처리하지 않음.)")
    @GetMapping("/{id}")
    @SwaggerApiError({ErrorStatus._CLIMBING_RECORD_NOT_FOUND})
    public ResponseEntity<ClimbingRecordDetailInfo> addClimbingRecord(
        @PathVariable Long id) {
        return ResponseEntity.ok(climbingRecordService.getClimbingRecordById(id));
    }


    @Operation(summary = "ClimbingRecord 수정")
    @PatchMapping("/{id}")
    @SwaggerApiError({ErrorStatus._CLIMBING_RECORD_NOT_FOUND, ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<ClimbingRecordSimpleInfo> updateClimbingRecord(
        @CurrentUser User user,
        @PathVariable Long id,
        @RequestBody UpdateClimbingRecordDto updateClimbingRecordDto) {
        return ResponseEntity.ok(
            climbingRecordService.updateClimbingRecord(user, id, updateClimbingRecordDto));
    }

    @Operation(summary = "ClimbingRecord 삭제")
    @DeleteMapping("/{id}")
    @SwaggerApiError({ErrorStatus._CLIMBING_RECORD_NOT_FOUND, ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<String> deleteClimbingRecord(
        @CurrentUser User user,
        @PathVariable Long id) {
        climbingRecordService.deleteClimbingRecord(user, id);
        return ResponseEntity.ok("클라이밍 기록을 삭제하였습니다.");
    }

    @Operation(summary = "월별 운동기록 통계")
    @GetMapping("/statistics")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_RECORD, ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<ClimbingRecordStatisticsInfo> findClimbingStatistics(
        @CurrentUser User user,
        @RequestParam int year,
        @RequestParam int month) {
        return ResponseEntity.ok(climbingRecordService.getClimbingRecordStatistics(user, year, month));
    }
}