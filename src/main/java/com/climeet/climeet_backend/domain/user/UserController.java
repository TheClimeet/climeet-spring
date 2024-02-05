package com.climeet.climeet_backend.domain.user;

import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserFollowDetailInfo;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="User")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

    @PostMapping("/refresh-token")
    @Operation(summary = "소셜 Access token, Refresh token 재발급 ")
    @SwaggerApiError({ErrorStatus._INVALID_JWT, ErrorStatus._EXPIRED_JWT, ErrorStatus._INVALID_MEMBER})
    public ResponseEntity<UserTokenSimpleInfo> refreshToken(@RequestParam String refreshToken){
        UserTokenSimpleInfo userTokenSimpleInfo = userService.updateUserToken(refreshToken);
        return ResponseEntity.ok(userTokenSimpleInfo);


    }
    @GetMapping("/get-follower")
    @Operation(summary = "특정 유저 암장 팔로워 조회")
    public ResponseEntity<List<UserFollowDetailInfo>> getFollower(@RequestParam Long userId, @RequestParam String userCategory, @CurrentUser User currentUser){
        List<UserFollowDetailInfo> userFollowDetailResponseList = userService.getFollower(userId, currentUser, userCategory);
        return ResponseEntity.ok(userFollowDetailResponseList);

    }

//    @GetMapping("/get-following")
//    @Operation(summary = "특정 유저 암장 팔로잉 조회")
//    public ResponseEntity<List<UserFollowDetailInfo>> getFollower(@RequestParam Long userId, @RequestParam String userCategory, @CurrentUser User currentUser){
//        List<UserFollowDetailInfo> userFollowDetailResponseList = userService.getFollowing(userId, currentUser);
//
//    }
}
