package com.climeet.climeet_backend.domain.user;

import com.climeet.climeet_backend.domain.user.dto.UserRequestDto.UpdateUserAllowNotificationRequest;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserAccountDetailInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserFollowDetailInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserFollowSimpleInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserHomeGymDetailInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserHomeGymSimpleInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserProfileDetailInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserTokenSimpleInfo;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/refresh-token")
    @Operation(summary = "소셜 Access token, Refresh token 재발급 ")
    @SwaggerApiError({ErrorStatus._INVALID_JWT, ErrorStatus._EXPIRED_JWT, ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<UserTokenSimpleInfo> refreshToken(@RequestParam String refreshToken){
        return ResponseEntity.ok(userService.updateUserToken(refreshToken));


    }

    @GetMapping("/followers")
    @Operation(summary = "특정 유저 팔로워 조회", description = "**userCategory** : Manager OR Climber")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._EMPTY_USER})
    public ResponseEntity<List<UserFollowDetailInfo>> getFollower(@RequestParam Long userId, @RequestParam String userCategory, @CurrentUser User currentUser){
        return ResponseEntity.ok(userService.getFollower(userId, currentUser, userCategory));

    }

    @GetMapping("/followees")
    @Operation(summary = "특정 유저 팔로잉 조회", description = "**userCategory** : Manager OR Climber")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._EMPTY_USER})
    public ResponseEntity<List<UserFollowDetailInfo>> getFollowing(@RequestParam Long userId, @RequestParam String userCategory, @CurrentUser User currentUser){
        return ResponseEntity.ok(userService.getFollowing(userId, currentUser, userCategory));

    }

    @GetMapping("/home/homegyms")
    @Operation(summary = "홈 화면 홈짐 바로가기")
    public ResponseEntity<List<UserHomeGymSimpleInfo>> getHomeGyms(@CurrentUser User currentUser) {
        return ResponseEntity.ok(userService.getHomeGyms(currentUser));
    }

    @GetMapping("/users/accounts")
    @Operation(summary = "로그인 계정 정보 조회")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._EMPTY_USER})
    public ResponseEntity<UserAccountDetailInfo> getLoginUserProfiles(
        @CurrentUser User currentUser) {
        return ResponseEntity.ok(userService.getLoginUserProfiles(currentUser));
    }

    @PatchMapping("/users/notifications")
    @Operation(summary = "notificaion 허용 범위 수정")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._EMPTY_USER})
    public ResponseEntity<String> updateUserNotification(@CurrentUser User currentUser, @RequestBody
    UpdateUserAllowNotificationRequest updateRequestDto) {
        return ResponseEntity.ok(userService.updateUserNotification(currentUser, updateRequestDto));
    }

    @GetMapping("/climber-following")
    @Operation(summary = "팔로우하는 클라이머 정보 조회(검색창 하단)")
    public ResponseEntity<List<UserFollowSimpleInfo>> getClimberFollowing(@CurrentUser User currentUser){
        return ResponseEntity.ok(userService.getClimberFollowing(currentUser));
    }

    @GetMapping("/gym-following")
    @Operation(summary = "팔로우하는 암장 정보 조회(검색창 하단)")
    public ResponseEntity<List<UserHomeGymDetailInfo>> getGymsFollowing(@CurrentUser User currentUser){
        return ResponseEntity.ok(userService.getGymsFollowing(currentUser));
    }

    @GetMapping("/profile")
    @SwaggerApiError({ErrorStatus._EMPTY_USER})
    @Operation(summary = "마이페이지 유저 프로필 조회")
    public ResponseEntity<UserProfileDetailInfo> getUserMyPageProfile(@CurrentUser User currentUser){
        return ResponseEntity.ok(userService.getUserMyPageProfile(currentUser));
    }
}
