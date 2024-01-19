package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.UpdateClimbingRecordDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.CreateClimbingRecordDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordSimpleInfo;
import com.climeet.climeet_backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
@RequestMapping("/climbing-record")
public class ClimbingRecordController {

    private final ClimbingRecordService climbingRecordService;


    @Operation(summary = "클라이밍 기록 생성")
    @PostMapping
    public ApiResponse<String> addClimbingRecord(@RequestBody CreateClimbingRecordDto requestDto) {
        climbingRecordService.createClimbingRecord(requestDto);
        return ApiResponse.onSuccess("클라이밍 기록을 생성하였습니다.");
    }

    @Operation(summary = "클라이밍 간편 기록 전체 조회")
    @GetMapping
    public ApiResponse<List<ClimbingRecordSimpleInfo>> getClimbingRecords() {
        return ApiResponse.onSuccess(climbingRecordService.getClimbingRecords());
    }


    @Operation(summary = "클라이밍 기록 날짜 조회")
    @GetMapping("/between-dates")
    public ApiResponse<List<ClimbingRecordSimpleInfo>> getClimbingRecordsBetweenDates(
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ClimbingRecordSimpleInfo> climbingRecords = climbingRecordService.getClimbingRecordsBetweenLocalDates(
            startDate, endDate);
        return ApiResponse.onSuccess(climbingRecords);
    }

    @Operation(summary = "클라이밍 기록 id 조회")
    @GetMapping("/{id}")
    public ApiResponse<ClimbingRecordSimpleInfo> addClimbingRecord(@PathVariable Long id) {
        return ApiResponse.onSuccess(climbingRecordService.getClimbingRecord(id));
    }


    @Operation(summary = "ClimbingRecord 수정")
    @PatchMapping("/{id}")
    public ApiResponse<ClimbingRecordSimpleInfo> updateClimbingRecord(
        @PathVariable Long id,
        @RequestBody UpdateClimbingRecordDto updateClimbingRecordDto) {
        return ApiResponse.onSuccess(
            climbingRecordService.updateClimbingRecord(id, updateClimbingRecordDto));
    }

    @Operation(summary = "ClimbingRecord 삭제")
    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteClimbingRecord(@PathVariable Long id) {
        climbingRecordService.deleteClimbingRecord(id);
        return ApiResponse.onSuccess("클라이밍 기록을 삭제하였습니다.");
    }
}