package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordSimpleInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import java.io.IOException;

@Tag(name = "ClimbingRecords", description = "클라이밍 키록 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/climbing-record")
public class ClimbingRecordController {

    private final ClimbingRecordService climbingRecordService;

    /**
     * @param "ClimbingRecordRequestDto requestDto"
     */
    @Operation(summary = "클라이밍 기록 생성")
    @PostMapping
    public ResponseEntity<?> addClimbingRecord(@RequestBody ClimbingRecordRequestDto requestDto) {
        climbingRecordService.createClimbingRecord(requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 간편기록 총 조회
     *
     * @param
     * @return List<ClimbingRecordSimpleInfo>
     */
    @Operation(summary = "클라이밍 간편 기록 전체 조회")
    @GetMapping
    public ResponseEntity<List<ClimbingRecordSimpleInfo>> getClimbingRecords() {
        return new ResponseEntity<>(climbingRecordService.getClimbingRecords(),
            HttpStatus.OK);
    }

    /**
     * @param "starDate", "endDate"
     * @return List<ClimbingRecordSimpleInfo>
     */
    @Operation(summary = "클라이밍 기록 날짜 조회")
    @GetMapping("/between-dates")
    public ResponseEntity<List<ClimbingRecordSimpleInfo>> getClimbingRecordsBetweenDates(
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ClimbingRecordSimpleInfo> climbingRecords = climbingRecordService.getClimbingRecordsBetweenLocalDates(
            startDate, endDate);
        return ResponseEntity.ok(climbingRecords);
    }

    /**
     * @param "id"
     * @return ClimingRecordResponseDto
     */
    @Operation(summary = "클라이밍 기록 id 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ClimbingRecordSimpleInfo> addClimbingRecord(@PathVariable Long id) {
        return new ResponseEntity<>(climbingRecordService.getClimbingRecord(id), HttpStatus.OK);
    }


}