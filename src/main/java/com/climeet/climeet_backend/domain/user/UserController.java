package com.climeet.climeet_backend.domain.user;

import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserTokenSimpleInfo;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

}
