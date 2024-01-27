package com.climeet.climeet_backend.domain.shortsbookmark;

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
public class ShortsBookmarkController {

    private final ShortsBookmarkService shortsBookmarkService;

    @Operation(summary = "숏츠 북마크")
    @SwaggerApiError({ErrorStatus._EMPTY_SHORTS})
    @PatchMapping("/Shorts/{shortsId}/Bookmarked")
    public ResponseEntity<String> changeShortsBookmarked(
        @CurrentUser User user,
        @PathVariable Long shortsId) {
        shortsBookmarkService.changeShortsBookmarked(user, shortsId);
        return ResponseEntity.ok("숏츠 북마크에 성공했습니다.");
    }

    @Operation(summary = "숏츠 북마크")
    @SwaggerApiError({ErrorStatus._EMPTY_SHORTS})
    @PatchMapping("/Shorts/{shortsId}/UnBookmarked")
    public ResponseEntity<String> changeShortsUnBookmarked(
        @CurrentUser User user,
        @PathVariable Long shortsId) {
        shortsBookmarkService.changeShortsUnBookmarked(user, shortsId);
        return ResponseEntity.ok("숏츠 북마크 취소에 성공했습니다.");
    }
}