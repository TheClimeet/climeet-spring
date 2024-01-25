package com.climeet.climeet_backend.domain.shorts;

import com.climeet.climeet_backend.domain.shorts.dto.ShortsRequestDto.CreateShortsRequest;
import com.climeet.climeet_backend.domain.shorts.dto.ShortsResponseDto.ShortsDetailInfo;
import com.climeet.climeet_backend.domain.shorts.dto.ShortsResponseDto.ShortsSimpleInfo;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "shorts", description = "숏츠 API")
@RequiredArgsConstructor
@RestController
public class ShortsController {

    private final ShortsService shortsService;

    @PostMapping("/shorts")
    @Operation(summary = "숏츠 업로드")
    public ResponseEntity<String> uploadShorts(@RequestPart(value = "video") MultipartFile video,
        @RequestPart MultipartFile thumbnailImage, @RequestPart CreateShortsRequest createShortsRequest) {
        shortsService.uploadShorts(video, thumbnailImage, createShortsRequest);
        return ResponseEntity.ok("업로드 성공");
    }

    @GetMapping("/shorts/latest")
    @Operation(summary = "숏츠 최신순 조회")
    public ResponseEntity<PageResponseDto<List<ShortsSimpleInfo>>> findLatestShorts(@RequestParam int page,
        @RequestParam int size) {
        return ResponseEntity.ok(shortsService.findShortsLatest(page, size));
    }

    @GetMapping("/shorts/popular")
    @Operation(summary = "숏츠 인기순 조회")
    public ResponseEntity<PageResponseDto<List<ShortsSimpleInfo>>> findPopularShorts(@RequestParam int page,
        @RequestParam int size) {
        return ResponseEntity.ok(shortsService.findShortsPopular(page, size));
    }

    @GetMapping("/shorts/{shortsId}")
    @SwaggerApiError({ErrorStatus._EMPTY_SHORTS})
    @Operation(summary = "숏츠 상세 조회")
    public ResponseEntity<ShortsDetailInfo> findShorts(
        @CurrentUser User user, @PathVariable Long shortsId
    ) {
        return ResponseEntity.ok(shortsService.findShorts(user, shortsId));
    }
}