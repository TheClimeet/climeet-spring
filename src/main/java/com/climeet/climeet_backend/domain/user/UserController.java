package com.climeet.climeet_backend.domain.user;

import com.climeet.climeet_backend.domain.user.dto.UserRequestDto.UpdateUserAllowNotificationRequest;
import com.climeet.climeet_backend.domain.user.dto.UserRequestDto.UpdateUserFcmToken;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserAccountDetailInfo;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserAllowNotificationInfo;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "User")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {
    private final UserService userService;

    @PostMapping("/refresh-token")
    @Operation(summary = "소셜 Access token, Refresh token 재발급 - 2201 [미리]")
    @SwaggerApiError({ErrorStatus._INVALID_JWT, ErrorStatus._EXPIRED_JWT, ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<UserTokenSimpleInfo> refreshToken(@RequestParam String refreshToken){
        return ResponseEntity.ok(userService.updateUserToken(refreshToken));


    }
    @GetMapping("/followers")
    @Operation(summary = "특정 유저 팔로워 조회 - 2202 [미리]", description = "**userCategory** : Manager OR Climber\n\n 다른 사람의 팔로워를 조회하고 싶을 때 followingUserId를 채워서 보내고, 나 자신의 팔로워를 조회하고 싶을 때는 null로 보내면 됩니다.")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._EMPTY_USER})
    public ResponseEntity<List<UserFollowDetailInfo>> getFollower(@RequestParam(required = false) Long userId, @RequestParam String userCategory, @CurrentUser User currentUser){
        return ResponseEntity.ok(userService.getFollower(userId, currentUser, userCategory));

    }

    @GetMapping("/followees")
    @Operation(summary = "특정 유저 팔로잉 조회 - 2203 [미리]", description = "**userCategory** : Manager OR Climber\n\n 다른 사람의 팔로잉를 조회하고 싶을 때 followingUserId를 채워서 보내고, 나 자신의 팔로잉를 조회하고 싶을 때는 null로 보내면 됩니다.")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._EMPTY_USER})
    public ResponseEntity<List<UserFollowDetailInfo>> getFollowing(@RequestParam(required = false) Long userId, @RequestParam String userCategory, @CurrentUser User currentUser){
        return ResponseEntity.ok(userService.getFollowing(userId, currentUser, userCategory));

    }

    @GetMapping("/home/homegyms")
    @Operation(summary = "홈 화면 홈짐 바로가기 - 2204 [미리]")
    public ResponseEntity<List<UserHomeGymSimpleInfo>> getHomeGyms(@CurrentUser User currentUser){
        return ResponseEntity.ok(userService.getHomeGyms(currentUser));
    }

    @GetMapping("/users/accounts")
    @Operation(summary = "로그인 계정 정보 조회 - 2205 [미리]")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._EMPTY_USER})
    public ResponseEntity<UserAccountDetailInfo> getLoginUserProfiles(
        @CurrentUser User currentUser) {
        return ResponseEntity.ok(userService.getLoginUserProfiles(currentUser));
    }

    @PatchMapping("/users/notifications")
    @Operation(summary = "notificaion 허용 범위 수정 - 2206 [미리]")
    @SwaggerApiError({ErrorStatus._BAD_REQUEST, ErrorStatus._EMPTY_USER})
    public ResponseEntity<String> updateUserNotification(@CurrentUser User currentUser, @RequestBody
    UpdateUserAllowNotificationRequest updateRequestDto) {
        return ResponseEntity.ok(userService.updateUserNotification(currentUser, updateRequestDto));
    }

    @GetMapping("/climber-following")
    @Operation(summary = "팔로우하는 클라이머 정보 조회(검색창 하단) - 2207 [미리]")
    public ResponseEntity<List<UserFollowSimpleInfo>> getClimberFollowing(@CurrentUser User currentUser){
        return ResponseEntity.ok(userService.getClimberFollowing(currentUser));
    }

    @GetMapping("/gym-following")
    @Operation(summary = "팔로우하는 암장 정보 조회(검색창 하단) - 2208 [미리]")
    public ResponseEntity<List<UserHomeGymDetailInfo>> getGymsFollowing(@CurrentUser User currentUser){
        return ResponseEntity.ok(userService.getGymsFollowing(currentUser));
    }

    @PostMapping("/users/fcmToken")
    @Operation(summary = "사용자 FCM 토큰 업데이트 - 2209 [미리]")
    public ResponseEntity<String> updateFcmToken(@CurrentUser User currentUser, @RequestBody
        UpdateUserFcmToken updateUserFcmToken){
        userService.updateUserFcmToken(currentUser, updateUserFcmToken.getFcmToken());
        return ResponseEntity.ok("업데이트 성공");
    }

    @GetMapping("/profile")
    @SwaggerApiError({ErrorStatus._EMPTY_USER})
    @Operation(summary = "마이페이지 유저 프로필 조회 - 2210 [미리]")
    public ResponseEntity<UserProfileDetailInfo> getUserMyPageProfile(@CurrentUser User currentUser){
        return ResponseEntity.ok(userService.getUserMyPageProfile(currentUser));
    }

    @GetMapping("/profile/{userId}")
    @SwaggerApiError({ErrorStatus._EMPTY_USER})
    @Operation(summary = "특정 유저 프로필 조회 - 2211 [미리]")
    public ResponseEntity<UserFollowDetailInfo> getUserMyPageProfile(@CurrentUser User currentUser, @PathVariable Long userId){
        return ResponseEntity.ok(userService.getOtherUserMyPageProfile(currentUser, userId));
    }

    @GetMapping("/home/homegyms/{userId}")
    @Operation(summary = "특정 유저 홈짐 조회 - 2212 [미리]")
    public ResponseEntity<List<UserHomeGymSimpleInfo>> getHomeGyms(@CurrentUser User currentUser, @PathVariable Long userId){
        return ResponseEntity.ok(userService.getUserHomeGyms(userId));
    }

    @PatchMapping("/profile-image")
    @Operation(summary = "유저 프로필 사진 업데이트 - 2213 [미리]")
    public ResponseEntity<String> updateUserProfileImage(@CurrentUser User currentUser, @RequestPart
        MultipartFile image){
        userService.updateUserProfileImage(currentUser, image);
        return ResponseEntity.ok("프로필 사진 업데이트 완료");
    }

    @PatchMapping("/profile-name")
    @Operation(summary = "유저 프로필 이름 업데이트 - 2214 [미리]")
    public ResponseEntity<String> updateUserProfileName(@CurrentUser User currentUser, @RequestParam String name){
        userService.updateUserProfileName(currentUser, name);
        return ResponseEntity.ok("프로필 이름 업데이트 완료");
    }
    @GetMapping("/users/notifications")
    @Operation(summary = "유저 알림 허용 범위 조회 - 2215 [미리]")
    public ResponseEntity<UserAllowNotificationInfo> getUserNotification(@CurrentUser User currentUser){
        return ResponseEntity.ok(userService.getUserNotification(currentUser));
    }
//    @PostMapping("/master-token")
//    public String createMasterToken(){
//        return userService.createMasterToken();
//    }
}
