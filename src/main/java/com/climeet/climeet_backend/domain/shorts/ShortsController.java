package com.climeet.climeet_backend.domain.shorts;

import com.climeet.climeet_backend.domain.shorts.dto.ShortsRequestDto.CreateShortsRequest;
import com.climeet.climeet_backend.domain.shorts.dto.ShortsResponseDto.ShortsProfileSimpleInfo;
import com.climeet.climeet_backend.domain.shorts.dto.ShortsResponseDto.ShortsSimpleInfo;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import com.google.firebase.messaging.FirebaseMessagingException;
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

@Tag(name = "Shorts", description = "숏츠 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ShortsController {

    private final ShortsService shortsService;

    @PostMapping("/shorts")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_SECTOR,
        ErrorStatus._EMPTY_ROUTE})
    @Operation(summary = "숏츠 업로드 - 301 [진로]", description = "**shortsVisibility** : PUBLIC OR FOLLOWERS_ONLY OR PRIVATE")
    public ResponseEntity<String> uploadShorts(@CurrentUser User user,
        @RequestPart(value = "video") MultipartFile video,
        @RequestPart CreateShortsRequest createShortsRequest) throws FirebaseMessagingException {
        shortsService.uploadShorts(user, video, createShortsRequest);
        return ResponseEntity.ok("업로드 성공");
    }

    @GetMapping("/shorts/latest")
    @Operation(summary = "숏츠 최신순 조회 - 305 [진로]", description = "gymId, sectorId, routeIds 미 입력시 전체 조회")
    public ResponseEntity<PageResponseDto<List<ShortsSimpleInfo>>> findLatestShorts(
        @CurrentUser User user, @RequestParam int page, @RequestParam int size,
        @RequestParam(required = false) Long gymId, @RequestParam(required = false) Long sectorId,
        @RequestParam(required = false) Long routeId) {
        return ResponseEntity.ok(
            shortsService.findShortsLatest(user, gymId, sectorId, routeId, page, size));
    }

    @GetMapping("/shorts/popular")
    @Operation(summary = "숏츠 인기순 조회 - 307 [진로]", description = "gymId, sectorId, routeIds 미 입력시 전체 조회")
    public ResponseEntity<PageResponseDto<List<ShortsSimpleInfo>>> findPopularShorts(
        @CurrentUser User user, @RequestParam int page, @RequestParam int size,
        @RequestParam(required = false) Long gymId, @RequestParam(required = false) Long sectorId,
        @RequestParam(required = false) Long routeId) {
        return ResponseEntity.ok(
            shortsService.findShortsPopular(user, gymId, sectorId, routeId, page, size));
    }

    @PatchMapping("/shorts/{shortsId}/viewCount")
    @Operation(summary = "숏츠 조회수 증가 - 303 [진로]")
    public ResponseEntity<String> updateShortsViewCount(@CurrentUser User user,
        @PathVariable Long shortsId) {
        shortsService.updateShortsViewCount(user, shortsId);
        return ResponseEntity.ok("조회수 증가에 성공했습니다.");
    }

    @GetMapping("/shorts/profile")
    @Operation(summary = "숏츠 프로필 바 조회 - 308 [진로]", description = "팔로우 하고있는 암장, 프로필 리스트 조회/최근에 영상을 올렸을 시 true")
    public ResponseEntity<List<ShortsProfileSimpleInfo>> getShortsProfileList(
        @CurrentUser User user) {
        List<ShortsProfileSimpleInfo> shortsProfileSimpleInfoList = shortsService.getShortsProfileList(
            user);
        return ResponseEntity.ok(shortsProfileSimpleInfoList);
    }

    @PatchMapping("/shorts/isRead")
    @Operation(summary = "숏츠 프로필바 초록불 OFF 처리 - 304 [진로]")
    @SwaggerApiError({ErrorStatus._EMPTY_FOLLOW_RELATIONSHIP})
    public ResponseEntity<String> updateShortsIsRead(@CurrentUser User user,
        @RequestParam Long followingUserId) {
        shortsService.updateShortsIsRead(user, followingUserId);
        return ResponseEntity.ok("초록불 OFF 완료");
    }

    @GetMapping("/shorts/{shortsId}")
    @Operation(summary = "숏츠 단일 조회 - 302 [진로]")
    @SwaggerApiError({ErrorStatus._EMPTY_SHORTS, ErrorStatus._SHORTS_ACCESS_DENIED})
    public ResponseEntity<ShortsSimpleInfo> findShorts(@CurrentUser User user,
        @PathVariable Long shortsId) {
        return ResponseEntity.ok(shortsService.findDetailShorts(user, shortsId));
    }

    @GetMapping("/shorts/uploader/{uploaderId} - 309 [진로]")
    @Operation(summary = "특정 유저가 올린 숏츠 조회")
    @SwaggerApiError({ErrorStatus._EMPTY_USER})
    public ResponseEntity<PageResponseDto<List<ShortsSimpleInfo>>> findShortsByUserId(@CurrentUser User user,
        @PathVariable Long uploaderId,
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam SortType sortType
        ) {
        return ResponseEntity.ok(shortsService.findShortsByUserIdAndSortType(user, uploaderId, sortType, page, size));
    }

    @GetMapping("/shorts/my-shorts")
    @Operation(summary = "내 숏츠 조회 - 306 [진로]")
    public ResponseEntity<PageResponseDto<List<ShortsSimpleInfo>>> findShortsByLoginUser(@CurrentUser User user,
        @RequestParam ShortsVisibility shortsVisibility,
        @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(shortsService.findMyShortsByShortsVisibility(user, shortsVisibility, page, size));
    }

    @GetMapping("/shorts/user/liked")
    @Operation(summary = "내가 좋아요 누른 숏츠 조회 - 310 [진로]")
    public ResponseEntity<PageResponseDto<List<ShortsSimpleInfo>>> findUserLikedShorts(@CurrentUser User user,
        @RequestParam int page, @RequestParam int size) {
        return ResponseEntity.ok(shortsService.findUserLikedShorts(user, page, size));
    }

    @Operation(summary = "내가 북마크 숏츠 조회 - 311 [진로]")
    @GetMapping("/shorts/user/bookmarked")
    public ResponseEntity<PageResponseDto<List<ShortsSimpleInfo>>> findUserBookmarkedShorts(
        @CurrentUser User user,
        @RequestParam int page,
        @RequestParam int size) {
        PageResponseDto<List<ShortsSimpleInfo>> response = shortsService.findUserBookmarkedShorts(user, page, size);
        return ResponseEntity.ok(response);
    }
}