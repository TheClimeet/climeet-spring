package com.climeet.climeet_backend.global.s3;

import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.s3.dto.S3Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@Tag(name = "S3")
public class S3Controller {

    private final S3Service s3Service;

    @PostMapping("api/file")
    @Operation(summary = "2301 [진로]")
    public ResponseEntity<S3Result> uploadFile(@RequestPart(value = "file") MultipartFile file) {
        try {
            S3Result result = s3Service.uploadFile(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._FILE_UPLOAD_ERROR);
        }
    }

    @PostMapping("api/retool/file")
    public ResponseEntity<S3Result> uploadRetoolFile(@RequestParam("file") MultipartFile file) {
        try {
            S3Result result = s3Service.uploadFile(file);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._FILE_UPLOAD_ERROR);
        }
    }
}