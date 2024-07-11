package com.climeet.climeet_backend.domain.blockeduser;

import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.UserRepository;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class BlockUserController {

    private final BlockUserService blockUserService;
    private final UserRepository userRepository;

    @PostMapping("/users/{userId}/block")
    @SwaggerApiError({ErrorStatus._EMPTY_USER, ErrorStatus._ALREADY_BLOCKED})
    @Operation(summary = "유저 차단하기 - 2901 [진로]", description = "id : 차단할 유저의 id")
    public ResponseEntity<String> blockUser(@CurrentUser User blocker, @PathVariable Long userId) {
        User blocked = userRepository.findById(userId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_USER));
        blockUserService.blockUser(blocker, blocked);
        return ResponseEntity.ok("유저 차단이 완료되었습니다.");
    }
}
