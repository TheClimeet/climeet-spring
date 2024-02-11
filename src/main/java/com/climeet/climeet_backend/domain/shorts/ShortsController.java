package com.climeet.climeet_backend.domain.shorts;

import com.climeet.climeet_backend.domain.shorts.dto.ShortsRequestDto.CreateShortsRequest;
import com.climeet.climeet_backend.domain.shorts.dto.ShortsResponseDto.ShortsProfileSimpleInfo;
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
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "shorts", description = "숏츠 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ShortsController {

    private final ShortsService shortsService;

    @PostMapping("/shorts")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_SECTOR,
        ErrorStatus._EMPTY_ROUTE})
    @Operation(summary = "숏츠 업로드", description = "**shortsVisibility** : PUBLIC OR FOLLOWERS_ONLY OR PRIVATE")
    public ResponseEntity<String> uploadShorts(@CurrentUser User user,
        @RequestPart(value = "video") MultipartFile video,
        @RequestPart MultipartFile thumbnailImage,
        @RequestPart CreateShortsRequest createShortsRequest) {
        shortsService.uploadShorts(user, video, thumbnailImage, createShortsRequest);
        return ResponseEntity.ok("업로드 성공");
    }

    @GetMapping("/shorts/latest")
    @Operation(summary = "숏츠 최신순 조회", description = "gymId, sectorId, routeIds 미 입력시 전체 조회")
    public ResponseEntity<PageResponseDto<List<ShortsSimpleInfo>>> findLatestShorts(
        @CurrentUser User user, @RequestParam int page, @RequestParam int size,
        @RequestParam(required = false) Long gymId, @RequestParam(required = false) Long sectorId,
        @RequestParam(required = false) List<Long> routeIds) {
        return ResponseEntity.ok(
            shortsService.findShortsLatest(user, gymId, sectorId, routeIds, page, size));
    }

    @GetMapping("/shorts/popular")
    @Operation(summary = "숏츠 인기순 조회", description = "gymId, sectorId, routeIds 미 입력시 전체 조회")
    public ResponseEntity<PageResponseDto<List<ShortsSimpleInfo>>> findPopularShorts(
        @CurrentUser User user, @RequestParam int page, @RequestParam int size,
        @RequestParam(required = false) Long gymId, @RequestParam(required = false) Long sectorId,
        @RequestParam(required = false) List<Long> routeIds) {
        return ResponseEntity.ok(shortsService.findShortsPopular(user, gymId, sectorId, routeIds, page, size));
    }

    @PatchMapping("/shorts/{shortsId}/viewCount")
    @Operation(summary = "숏츠 조회수 증가")
    public ResponseEntity<String> updateShortsViewCount(@CurrentUser User user,
        @PathVariable Long shortsId) {
        shortsService.updateShortsViewCount(user, shortsId);
        return ResponseEntity.ok("조회수 증가에 성공했습니다.");
    }

    @GetMapping("/shorts/profile")
    @Operation(summary = "숏츠 프로필 바 조회", description = "팔로우 하고있는 암장, 프로필 리스트 조회/최근에 영상을 올렸을 시 true")
    public ResponseEntity<List<ShortsProfileSimpleInfo>> getShortsProfileList(
        @CurrentUser User user) {
        List<ShortsProfileSimpleInfo> shortsProfileSimpleInfoList = shortsService.getShortsProfileList(
            user);
        return ResponseEntity.ok(shortsProfileSimpleInfoList);
    }
}