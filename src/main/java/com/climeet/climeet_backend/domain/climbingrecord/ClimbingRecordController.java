package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.UpdateClimbingRecordDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto.CreateClimbingRecordDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordSimpleInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
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
@RequestMapping("/climbing-record")
public class ClimbingRecordController {

    private final ClimbingRecordService climbingRecordService;

    @Operation(summary = "클라이밍 기록 생성")
    @PostMapping
    public ResponseEntity<?> addClimbingRecord(@RequestBody CreateClimbingRecordDto requestDto) {
        climbingRecordService.createClimbingRecord(requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "클라이밍 간편 기록 전체 조회")
    @GetMapping
    public ResponseEntity<List<ClimbingRecordSimpleInfo>> getClimbingRecords() {
        return new ResponseEntity<>(climbingRecordService.getClimbingRecords(),
            HttpStatus.OK);
    }


    @Operation(summary = "클라이밍 기록 날짜 조회")
    @GetMapping("/between-dates")
    public ResponseEntity<List<ClimbingRecordSimpleInfo>> getClimbingRecordsBetweenDates(
        @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
        @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ClimbingRecordSimpleInfo> climbingRecords = climbingRecordService.getClimbingRecordsBetweenLocalDates(
            startDate, endDate);
        return ResponseEntity.ok(climbingRecords);
    }

    @Operation(summary = "클라이밍 기록 id 조회")
    @GetMapping("/{id}")
    public ResponseEntity<ClimbingRecordSimpleInfo> addClimbingRecord(@PathVariable Long id) {
        return new ResponseEntity<>(climbingRecordService.getClimbingRecord(id), HttpStatus.OK);
    }


    @Operation(summary = "ClimbingRecord 수정")
    @PatchMapping("/{id}")
    public ResponseEntity<ClimbingRecordSimpleInfo> updateClimbingRecord(
        @PathVariable Long id,
        @RequestBody UpdateClimbingRecordDto updateClimbingRecordDto) {
        return new ResponseEntity<>(
            climbingRecordService.updateClimbingRecord(id, updateClimbingRecordDto), HttpStatus.OK);
    }

    @Operation(summary = "ClimbingRecord 삭제")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClimbingRecord(@PathVariable Long id){
        return new ResponseEntity<>(climbingRecordService.deleteClimbingRecord(id), HttpStatus.OK);
    }
}