package com.climeet.climeet_backend.domain.user;

import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserTokenSimpleInfo;
import com.climeet.climeet_backend.global.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name="User")
@RequiredArgsConstructor
@RestController
public class UserController {
    private final UserService userService;

//    @PostMapping("/refresh-token")
//    @Operation(description = "소셜 Access token, Refresh token 재발급 ")
//    public ResponseEntity<UserTokenSimpleInfo> refreshToken(String refreshToken){
//        UserTokenSimpleInfo userTokenSimpleInfo = userService.updateUserToken(refreshToken);
//        return ResponseEntity.ok(userTokenSimpleInfo);
//
//
//    }

}
