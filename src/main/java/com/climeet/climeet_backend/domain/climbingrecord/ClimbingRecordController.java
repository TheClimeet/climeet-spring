package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.UpdateClimbingRecord;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.CreateClimbingRecord;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.BestClearUserSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.BestLevelUserSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.BestTimeUserSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordDetailInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordStatisticsInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordStatisticsInfoByGym;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordStatisticsSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordUserAndGymStatisticsDetailInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordUserStatisticsSimpleInfo;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.VisitedClimbingGym;
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
@RequestMapping("api/climbing-records")
public class ClimbingRecordController {

    private final ClimbingRecordService climbingRecordService;


    @Operation(summary = "클라이밍 기록 생성")
    @PostMapping
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_ROUTE})
    public ResponseEntity<String> createClimbingRecord(
        @CurrentUser User user,
        @RequestBody CreateClimbingRecord requestDto) {
        climbingRecordService.createClimbingRecord(user, requestDto);
        return ResponseEntity.ok("클라이밍 기록을 생성하였습니다.");
    }

    @Operation(summary = "클라이밍 간편 기록 전체 조회")
    @GetMapping
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_RECORD})
    public ResponseEntity<List<ClimbingRecordSimpleInfo>> getClimbingRecordList(
        @CurrentUser User user
    ) {
        return ResponseEntity.ok(climbingRecordService.getClimbingRecordList());
    }

    @Operation(summary = "클라이밍 기록 id 조회 (루트기록들 포함. 단, 루트 기록은 없어도 예외처리하지 않음.)")
    @GetMapping("/{id}")
    @SwaggerApiError({ErrorStatus._CLIMBING_RECORD_NOT_FOUND})
    public ResponseEntity<ClimbingRecordDetailInfo> getClimbingRecordById(
        @CurrentUser User user,
        @PathVariable Long id) {
        return ResponseEntity.ok(climbingRecordService.getClimbingRecordById(id));
    }


    @Operation(summary = "나의 클라이밍 기록 날짜 조회")
    @GetMapping("/between-dates")
    @SwaggerApiError({ErrorStatus._INVALID_DATE_RANGE,
        ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<List<ClimbingRecordSimpleInfo>> getClimbingRecordListBetweenDates(
        @CurrentUser User user,
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ClimbingRecordSimpleInfo> climbingRecords
            = climbingRecordService.getClimbingRecordListBetweenDates(user, startDate, endDate);
        return ResponseEntity.ok(climbingRecords);
    }


    @Operation(summary = "ClimbingRecord 수정")
    @PatchMapping("/{id}")
    @SwaggerApiError({ErrorStatus._CLIMBING_RECORD_NOT_FOUND, ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<ClimbingRecordSimpleInfo> updateClimbingRecord(
        @CurrentUser User user,
        @PathVariable Long id,
        @RequestBody UpdateClimbingRecord updateClimbingRecord) {
        return ResponseEntity.ok(
            climbingRecordService.updateClimbingRecord(user, id, updateClimbingRecord));
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

    @Operation(summary = "나의 월별 운동기록 통계")
    @GetMapping("/users/statistics/months")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_RECORD, ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<ClimbingRecordStatisticsInfo> getClimbingStatisticsMonthly(
        @CurrentUser User user,
        @RequestParam int year,
        @RequestParam int month) {
        return ResponseEntity.ok(
            climbingRecordService.getClimbingRecordStatistics(user, year, month));
    }

    @Operation(summary = "특정 암장에 대한 나의 월 통계")
    @GetMapping("/users/gyms/{gymId}/statistics/months")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_RECORD, ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<ClimbingRecordStatisticsInfoByGym> getClimbingStatisticsByGymMonthly(
        @CurrentUser User user,
        @PathVariable Long gymId,
        @RequestParam int year,
        @RequestParam int month) {
        return ResponseEntity.ok(
            climbingRecordService.getClimbingRecordStatisticsByGymId(user, gymId, year, month));
    }

    @Operation(summary = "암장별 주간 평균 완등률 통계 ")
    @GetMapping("/gyms/{gymId}/statistics/weeks")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM})
    public ResponseEntity<ClimbingRecordStatisticsSimpleInfo> getGymStatisticsWeekly(
        @CurrentUser User user,
        @PathVariable Long gymId
    ) {
        return ResponseEntity.ok(
            climbingRecordService.getGymStatisticsWeekly(gymId));
    }

    @Operation(summary = "[완등순] 암장별 클라이머 랭킹")
    @GetMapping("/gyms/{gymId}/rank/weeks/climbers/clear")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM})
    public ResponseEntity<List<BestClearUserSimpleInfo>> getClimberRankingListOrderClearCountByGym(
        @CurrentUser User user,
        @PathVariable Long gymId) {
        return ResponseEntity.ok(climbingRecordService.getClimberRankingListOrderClearCountByGym(gymId));
    }

    @Operation(summary = "[시간순] 암장별 클라이머 랭킹")
    @GetMapping("/gyms/{gymId}/rank/weeks/climbers/time")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM})
    public ResponseEntity<List<BestTimeUserSimpleInfo>> getClimberRankingListOrderTimeByGym(
        @CurrentUser User user,
        @PathVariable Long gymId) {
        return ResponseEntity.ok(climbingRecordService.getClimberRankingListOrderTimeByGym(gymId));
    }

    @Operation(summary = "[높은 레벨순] 암장별 클라이머 랭킹")
    @GetMapping("/gyms/{gymId}/rank/weeks/climbers/level")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM})
    public ResponseEntity<List<BestLevelUserSimpleInfo>> getClimberRankingListOrderLevelByGym(
        @CurrentUser User user,
        @PathVariable Long gymId) {
        return ResponseEntity.ok(climbingRecordService.getClimberRankingListOrderLevelByGym(gymId));
    }

    @Operation(summary = "[유저프로필] 유저별 전체 암장에 대한 누적 통계")
    @GetMapping("/users/{userId}/statistics")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM})
    public ResponseEntity<ClimbingRecordUserStatisticsSimpleInfo> getUserStatistics(
        @CurrentUser User user,
        @PathVariable Long userId) {
        return ResponseEntity.ok(climbingRecordService.getUserStatistics(userId));
    }

    @Operation(summary = "유저별 특정 암장에 대한 누적 통계")
    @GetMapping("/users/{userId}/gyms/{gymId}/statistics")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM})
    public ResponseEntity<ClimbingRecordUserAndGymStatisticsDetailInfo> getUserStatistics(
        @CurrentUser User user,
        @PathVariable Long userId,
        @PathVariable Long gymId
    ) {
        return ResponseEntity.ok(climbingRecordService.getUserStatisticsByGym(userId, gymId));
    }

    @Operation(summary = "유저별 운동한 암장 리스트 조회")
    @GetMapping("/users/{userId}/months/list")
    public ResponseEntity<List<VisitedClimbingGym>> getVisitedGymList(
            @CurrentUser User user,
            @PathVariable Long userId,
            @RequestParam int year,
            @RequestParam int month) {
        return ResponseEntity.ok(climbingRecordService.getVisitedGymList(userId, year, month));
    }

    @Operation(summary = "나의 운동한 암장 리스트 조회")
    @GetMapping("/users/months/list")
    public ResponseEntity<List<VisitedClimbingGym>> getVisitedGymList(
        @CurrentUser User user,
        @RequestParam int year,
        @RequestParam int month) {
        return ResponseEntity.ok(climbingRecordService.getVisitedGymList(user.getId(), year, month));
    }
}