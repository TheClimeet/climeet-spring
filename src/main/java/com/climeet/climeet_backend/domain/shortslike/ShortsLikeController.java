package com.climeet.climeet_backend.domain.shortslike;

import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ShortsLikeController {

    private final ShortsLikeService shortsLikeService;

    @Operation(summary = "숏츠 좋아요")
    @SwaggerApiError({ErrorStatus._EMPTY_SHORTS})
    @PatchMapping("/Shorts/{shortsId}/likes")
    public ResponseEntity<String> changeShortsLikeStatus(
        @CurrentUser User user,
        @PathVariable Long shortsId) {
        shortsLikeService.changeShortsLikeStatus(user, shortsId);
        return ResponseEntity.ok("숏츠 좋아요에 성공했습니다.");
    }

}