package com.climeet.climeet_backend.domain.user;

import com.climeet.climeet_backend.domain.user.dto.UserRequestDto.UpdateUserAllowNotificationRequest;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserAccountDetailInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserFollowDetailInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserHomeGymSimpleInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserTokenSimpleInfo;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.security.JwtTokenProvider;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.jsonwebtoken.ExpiredJwtException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "User")
@RequiredArgsConstructor
@RestController
public class UserController {

    private final UserService userService;

    @PostMapping("/refresh-token")
    @Operation(summary = "소셜 Access token, Refresh token 재발급 ")
    @SwaggerApiError({ErrorStatus._INVALID_JWT, ErrorStatus._EXPIRED_JWT,
        ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<UserTokenSimpleInfo> refreshToken(@RequestParam String refreshToken) {
        UserTokenSimpleInfo userTokenSimpleInfo = userService.updateUserToken(refreshToken);
        return ResponseEntity.ok(userTokenSimpleInfo);


    }

    @GetMapping("/followers")
    @Operation(summary = "특정 유저 팔로워 조회", description = "**userCategory** : Manager OR Climber")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._EMPTY_USER})
    public ResponseEntity<List<UserFollowDetailInfo>> getFollower(@RequestParam Long userId,
        @RequestParam String userCategory, @CurrentUser User currentUser) {
        List<UserFollowDetailInfo> userFollowDetailResponseList = userService.getFollower(userId,
            currentUser, userCategory);
        return ResponseEntity.ok(userFollowDetailResponseList);

    }

    @GetMapping("/followees")
    @Operation(summary = "특정 유저 팔로잉 조회", description = "**userCategory** : Manager OR Climber")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._EMPTY_USER})
    public ResponseEntity<List<UserFollowDetailInfo>> getFollowing(@RequestParam Long userId,
        @RequestParam String userCategory, @CurrentUser User currentUser) {
        List<UserFollowDetailInfo> userFollowDetailResponseList = userService.getFollowing(userId,
            currentUser, userCategory);
        return ResponseEntity.ok(userFollowDetailResponseList);

    }

    @GetMapping("/home/homegyms")
    @Operation(summary = "홈 화면 홈짐 바로가기")
    public ResponseEntity<List<UserHomeGymSimpleInfo>> getHomeGyms(@CurrentUser User currentUser) {
        return ResponseEntity.ok(userService.getHomeGyms(currentUser));
    }

    @GetMapping("/users/accounts")
    @Operation(summary = "로그인 계정 정보")
    public ResponseEntity<UserAccountDetailInfo> getLoginUserProfiles(
        @CurrentUser User currentUser) {
        return ResponseEntity.ok(userService.getLoginUserProfiles(currentUser));
    }

    @PatchMapping("/users/notification")
    @Operation(summary = "notificaion 허용 범위 수정")
    public ResponseEntity<String> updateUserNotification(@CurrentUser User currentUser, @RequestBody
    UpdateUserAllowNotificationRequest updateRequestDto) {
        return ResponseEntity.ok(userService.updateUserNotification(currentUser, updateRequestDto));
    }
}
