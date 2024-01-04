package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @Operation(summary = "클라이밍기록생성", description = "클라이밍 기록 생성")
    @PostMapping
    public ResponseEntity<?> addClimbingRecord(@RequestBody ClimbingRecordRequestDto requestDto) {
        climbingRecordService.createClimbingRecord(requestDto);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}