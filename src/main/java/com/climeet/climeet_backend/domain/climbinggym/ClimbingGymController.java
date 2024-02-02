package com.climeet.climeet_backend.domain.climbinggym;

import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymRequestDto.UpdateClimbingGymInfoRequest;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.AcceptedClimbingGymSimpleResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymDetailResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymSimpleResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.LayoutDetailResponse;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "ClimbingGym", description = "암장 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/gym")
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

    @Operation(summary = "암장 도면 이미지 수정")
    @SwaggerApiError({ErrorStatus._EMPTY_MANAGER})
    @PostMapping("/layout")
    public ResponseEntity<LayoutDetailResponse> changeLayoutImage(
        @CurrentUser User user, @RequestPart MultipartFile layoutImage) {
        return ResponseEntity.ok(climbingGymService.changeLayoutImage(layoutImage, user));
    }

    @Operation(summary = "암장 프로필 정보 (상단) 불러오기")
    @SwaggerApiError({ErrorStatus._EMPTY_CLIMBING_GYM, ErrorStatus._EMPTY_MANAGER})
    @GetMapping("/{gymId}")
    public ResponseEntity<ClimbingGymDetailResponse> getClimbingGymInfo(
        @PathVariable Long gymId, @CurrentUser User user) {
        return ResponseEntity.ok(climbingGymService.getClimbingGymInfo(gymId));
    }

    @Operation(summary = "암장 크롤링 정보 입력")
    @PostMapping("/info")
    public ResponseEntity<ClimbingGymDetailResponse> updateClimbingGymInfo(
        @RequestBody UpdateClimbingGymInfoRequest updateClimbingGymInfoRequest) {
        return ResponseEntity.ok(climbingGymService.updateClimbingGymInfo(updateClimbingGymInfoRequest));
    }

}