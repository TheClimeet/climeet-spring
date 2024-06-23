package com.climeet.climeet_backend.domain.climbinggym;

import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymRequestDto.UpdateClimbingGymPriceRequest;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymRequestDto.UpdateClimbingGymServiceRequest;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.AcceptedClimbingGymSimpleResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.AcceptedClimbingGymSimpleResponseWithFollow;
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

    @Operation(summary = "전국 암장 검색 기능(자체 목록화) - 1010 [무빗]")
    @GetMapping("/search/all")
    public ResponseEntity<PageResponseDto<List<ClimbingGymSimpleResponse>>> getClimbingGymSearchingList(
        @RequestParam("gymname") String gymName, @RequestParam int page, @RequestParam int size
    ) {
        return ResponseEntity.ok(climbingGymService.searchClimbingGym(gymName, page, size));
    }

    @Operation(summary = "Manager가 등록된 암장 검색 기능 - 1009 [무빗]")
    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<List<AcceptedClimbingGymSimpleResponse>>> getAcceptedClimbingGymSearchingList(
        @RequestParam("gymname") String gymName, @RequestParam int page, @RequestParam int size
    ) {
        return ResponseEntity.ok(climbingGymService.searchAcceptedClimbingGym(gymName, page, size));
    }

    @Operation(summary = "암장 프로필 정보 (상단) 불러오기 - 1001 [무빗]")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_MANAGER,
        ErrorStatus._EMPTY_BACKGROUND_IMAGE})
    @GetMapping("/{gymId}")
    public ResponseEntity<ClimbingGymDetailResponse> getClimbingGymInfo(
        @PathVariable Long gymId, @CurrentUser User user) {
        return ResponseEntity.ok(climbingGymService.getClimbingGymInfo(gymId, user));
    }


    @Operation(summary = "암장 프로필 정보 (탭) 불러오기 - 1005 [무빗]", description = "탭에 들어가는 데이터는 null이 존재할 가능성이 상당히 높습니다.\n 또한 영업시간에서 특정 요일만 값이 없다면 정기휴일입니다.")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._ERROR_JSON_PARSE})
    @GetMapping("/{gymId}/tab")
    public ResponseEntity<ClimbingGymTabInfoResponse> getClimbingGymTabInfo(
        @PathVariable Long gymId, @CurrentUser User user) {
        return ResponseEntity.ok(climbingGymService.getClimbingGymTabInfo(gymId));
    }

    @Operation(summary = "암장 크롤링 정보 입력 - 1002 [무빗]")
    @PostMapping("/{gymId}/info")
    public ResponseEntity<ClimbingGymInfoResponse> updateClimbingGymInfo(
        @PathVariable Long gymId) {
        return ResponseEntity.ok(
            climbingGymService.updateClimbingGymInfo(gymId));
    }

    @Operation(summary = "암장 실력분포 조회")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_AVERAGE_LEVEL_DATA})
    @GetMapping("/{gymId}/skill-distribution")
    public ResponseEntity<List<ClimbingGymAverageLevelDetailResponse>> getFollowingUserAverageLevelInClimbingGym(
        @PathVariable Long gymId, @CurrentUser User user) {
        return ResponseEntity.ok(
            climbingGymService.getFollowingUserAverageLevelInClimbingGym(gymId, user));
    }

    @Operation(summary = "암장 배경사진 변경 (1개만 등록 가능) - 1006 [무빗]")
    @SwaggerApiError({ErrorStatus._EMPTY_MANAGER, ErrorStatus._EMPTY_BACKGROUND_IMAGE})
    @PatchMapping("/background-image")
    public ResponseEntity<String> changeClimbingGymBackgroundImage(@CurrentUser User user,
        @RequestPart MultipartFile image) {
        return ResponseEntity.ok(climbingGymService.changeClimbingGymBackgroundImage(user, image));
    }

    @Operation(summary = "암장 프로필 이미지 변경 (1개만 등록 가능) - 1008 [무빗]")
    @SwaggerApiError({ErrorStatus._EMPTY_MANAGER})
    @PatchMapping("/profile-image")
    public ResponseEntity<String> changeClimbingGymProfileImage(@CurrentUser User user,
        @RequestPart MultipartFile image) {
        return ResponseEntity.ok(climbingGymService.changeClimbingGymProfileImage(user, image));
    }

    @Operation(summary = "암장 제공 서비스 수정 - 1012 [무빗]", description = "**Enum 설명**\n\n**ServiceBitmask** :  샤워\\_시설,  샤워\\_용품,  수건\\_제공,  간이\\_세면대,  초크\\_대여,  암벽화\\_대여,  삼각대\\_대여,  운동복\\_대여")
    @SwaggerApiError({ErrorStatus._EMPTY_MANAGER})
    @PatchMapping("/service")
    public ResponseEntity<String> updateClimbingGymService(@CurrentUser User user,
        @RequestBody UpdateClimbingGymServiceRequest updateClimbingGymServiceRequest) {
        climbingGymService.updateClimbingGymService(user, updateClimbingGymServiceRequest);
        return ResponseEntity.ok("암장 제공 서비스를 정상적으로 변경했습니다.");
    }

    @Operation(summary = "기본 가격(제공) 추가 & 수정 - 1007 [무빗]")
    @SwaggerApiError({ErrorStatus._EMPTY_MANAGER, ErrorStatus._ERROR_JSON_PARSE})
    @PostMapping("/price")
    public ResponseEntity<String> updateClimbingGymPrice(@CurrentUser User user,
        @RequestBody UpdateClimbingGymPriceRequest updateClimbingGymPriceRequest) {
        climbingGymService.updateClimbingGymPrice(user, updateClimbingGymPriceRequest);
        return ResponseEntity.ok("암장 기본 제공을 정상적으로 변경했습니다.");
    }

    @Operation(summary = "Manager가 등록된 암장 검색 기능 + 팔로잉 여부 - 1011 [무빗]")
    @SwaggerApiError({})
    @GetMapping("/search/follow")
    public ResponseEntity<PageResponseDto<List<AcceptedClimbingGymSimpleResponseWithFollow>>> getAcceptedClimbingGymSearchingListWithFollow(
        @RequestParam("gymname") String gymName, @RequestParam int page, @RequestParam int size,
        @CurrentUser User user
    ) {
        return ResponseEntity.ok(
            climbingGymService.searchAcceptedClimbingGymWithFollow(gymName, page, size, user));
    }

    @Operation(summary = "특정 암장에서의 현재 내 실력 조회 - 1003 [무빗]")
    @SwaggerApiError({})
    @GetMapping("/{gymId}/my-skill")
    public ResponseEntity<String> getFollowingUserAverageLevelInClimbingGym(
        @CurrentUser User user, @PathVariable Long gymId) {
        return ResponseEntity.ok(climbingGymService.getClimberAverageDifficulty(user, gymId));
    }

}