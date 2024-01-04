package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordSimpleInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.joda.time.DateTime;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequiredArgsConstructor
@RestController("/climbing-record")
public class ClimbingRecordController {

    private final ClimbingRecordService climbingRecordService;

    /**
     * @param "ClimbingRecordRequestDto requestDto"
     */
    @Operation(summary = "클라이밍 기록 생성", description = "클라이밍 기록 생성")
    @PostMapping
    public ResponseEntity<?> addClimbingRecord(@RequestBody ClimbingRecordRequestDto requestDto) {
        climbingRecordService.createClimbingRecord(requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * 간편기록 총 조회
     *
     * @param
     * @return List<ClimbingRecordResponseDto>
     */
    @Operation(summary = "클라이밍 간편 기록 조회", description = "클라이밍 기록 생성")
    @GetMapping
    public ResponseEntity<List<ClimbingRecordSimpleInfo>> getClimbingRecordList() {
        return new ResponseEntity<>(climbingRecordService.getClimbingRecordSimpleInfoList(),
            HttpStatus.OK);
    }

    /**
     * @param "date"
     * @return ClimingRecordResponseDto
     */
    @Operation(summary = "클라이밍 기록 조회", description = "클라이밍 기록 생성")
    @GetMapping("/{date}")
    public ResponseEntity<List<ClimbingRecordSimpleInfo>> addClimbingRecord(
        @PathVariable LocalDate date) {
        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * @param "id"
     * @return ClimingRecordResponseDto
     */
    @Operation(summary = "클라이밍 기록 조회", description = "클라이밍 기록 생성")
    @GetMapping("/{id}")
    public ResponseEntity<ClimbingRecordSimpleInfo> addClimbingRecord(@PathVariable Long id) {
        return new ResponseEntity<>(climbingRecordService.getClimbingRecord(id), HttpStatus.OK);
    }

}