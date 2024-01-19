package com.climeet.climeet_backend.domain.shorts;

import com.climeet.climeet_backend.domain.shorts.dto.ShortsRequestDto.CreateShortsRequest;
import com.climeet.climeet_backend.domain.shorts.dto.ShortsResponseDto.ShortsSimpleInfo;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ApiResponse<String> uploadShorts(@RequestPart(value = "video") MultipartFile video,
        @RequestPart MultipartFile thumbnailImage, @RequestPart CreateShortsRequest createShortsRequest) {
        shortsService.uploadShorts(video, thumbnailImage, createShortsRequest);
        return ApiResponse.onSuccess("업로드 성공");
    }

    @GetMapping("/shorts/latest")
    @Operation(summary = "숏츠 최신순 조회")
    public ApiResponse<PageResponseDto<List<ShortsSimpleInfo>>> findLatestShorts(@RequestParam int page,
        @RequestParam int size) {
        return ApiResponse.onSuccess(shortsService.findShortsLatest(page, size));
    }

    @GetMapping("/shorts/popular")
    @Operation(summary = "숏츠 인기순 조회")
    public ApiResponse<PageResponseDto<List<ShortsSimpleInfo>>> findPopularShorts(@RequestParam int page,
        @RequestParam int size) {
        return ApiResponse.onSuccess(shortsService.findShortsPopular(page, size));
    }
}