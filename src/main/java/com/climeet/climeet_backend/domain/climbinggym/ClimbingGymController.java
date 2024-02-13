package com.climeet.climeet_backend.domain.climbinggym;

import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymRequestDto.UpdateClimbingGymServiceRequest;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.AcceptedClimbingGymSimpleResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymAverageLevelDetailResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymDetailResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymInfoResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymSimpleResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymTabInfoResponse;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.awt.Image;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "ClimbingGym", description = "암장 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/gyms")
public class ClimbingGymController {

    private final ClimbingGymService climbingGymService;

    @Operation(summary = "전국 암장 검색 기능(자체 목록화)")
    @GetMapping("/search/all")
    public ResponseEntity<PageResponseDto<List<ClimbingGymSimpleResponse>>> getClimbingGymSearchingList(
        @RequestParam("gymname") String gymName, @RequestParam int page, @RequestParam int size
    ) {
        return ResponseEntity.ok(climbingGymService.searchClimbingGym(gymName, page, size));
    }

    @Operation(summary = "Manager가 등록된 암장 검색 기능")
    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<List<AcceptedClimbingGymSimpleResponse>>> getAcceptedClimbingGymSearchingList(
        @RequestParam("gymname") String gymName, @RequestParam int page, @RequestParam int size
    ) {
        return ResponseEntity.ok(climbingGymService.searchAcceptedClimbingGym(gymName, page, size));
    }

    @Operation(summary = "암장 프로필 정보 (상단) 불러오기")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_MANAGER,
        ErrorStatus._EMPTY_BACKGROUND_IMAGE})
    @GetMapping("/{gymId}")
    public ResponseEntity<ClimbingGymDetailResponse> getClimbingGymInfo(
        @PathVariable Long gymId, @CurrentUser User user) {
        return ResponseEntity.ok(climbingGymService.getClimbingGymInfo(gymId));
    }

    @Operation(summary = "암장 프로필 정보 (탭) 불러오기")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._ERROR_JSON_PARSE})
    @GetMapping("/{gymId}/tab")
    public ResponseEntity<ClimbingGymTabInfoResponse> getClimbingGymTabInfo(
        @PathVariable Long gymId, @CurrentUser User user) {
        return ResponseEntity.ok(climbingGymService.getClimbingGymTabInfo(gymId));
    }

    @Operation(summary = "암장 크롤링 정보 입력")
    @PostMapping("/{gymId}/info")
    public ResponseEntity<ClimbingGymInfoResponse> updateClimbingGymInfo(
        @PathVariable Long gymId) {
        return ResponseEntity.ok(
            climbingGymService.updateClimbingGymInfo(gymId));
    }

    @Operation(summary = "암장 실력분포 조회")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_AVERAGE_LEVEL_DATA})
    @GetMapping("/{gymId}/graph/level")
    public ResponseEntity<List<ClimbingGymAverageLevelDetailResponse>> getFollowingUserAverageLevelInClimbingGym(
        @PathVariable Long gymId, @CurrentUser User user) {
        return ResponseEntity.ok(
            climbingGymService.getFollowingUserAverageLevelInClimbingGym(gymId));
    }

    @Operation(summary = "암장 배경사진 변경")
    @SwaggerApiError({})
    @PatchMapping("/{gymId}/background-image")
    public ResponseEntity<String> changeClimbingGymBackgroundImage(@PathVariable Long gymId,
        @CurrentUser User user, @RequestPart MultipartFile image) {
        return ResponseEntity.ok(climbingGymService.changeClimbingGymBackgroundImage(user, image));
    }

    @Operation(summary = "암장 프로필 이미지 변경")
    @SwaggerApiError({ErrorStatus._EMPTY_MANAGER})
    @PatchMapping("/{gymId}/background-image")
    public ResponseEntity<String> changeClimbingGymProfileImage(@PathVariable Long gymId,
        @CurrentUser User user, @RequestPart MultipartFile image) {
        return ResponseEntity.ok(climbingGymService.changeClimbingGymProfileImage(user, image));
    }

    @Operation(summary = "암장 제공 서비스 수정")
    @SwaggerApiError({ErrorStatus._EMPTY_MANAGER})
    @PatchMapping("/{gymId}/service")
    public ResponseEntity<String> updateClimbingGymService(@PathVariable Long gymId,
        @CurrentUser User user,
        @RequestBody UpdateClimbingGymServiceRequest updateClimbingGymServiceRequest) {
        climbingGymService.updateClimbingGymService(user, updateClimbingGymServiceRequest);
        return ResponseEntity.ok("암장 제공 서비스를 정상적으로 수정했습니다.");
    }

}