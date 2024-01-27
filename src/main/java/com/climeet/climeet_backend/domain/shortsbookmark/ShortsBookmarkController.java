package com.climeet.climeet_backend.domain.shortsbookmark;

import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ShortsBookmarkController {

    private final ShortsBookmarkService shortsBookmarkService;

    @Operation(summary = "숏츠 북마크")
    @SwaggerApiError({ErrorStatus._EMPTY_SHORTS})
    @PostMapping("/shorts/{shortsId}/bookmarks")
    public ResponseEntity<String> createShortsBookmark(
        @CurrentUser User user,
        @PathVariable Long shortsId) {
        shortsBookmarkService.createShortsBookmark(user, shortsId);
        return ResponseEntity.ok("숏츠 북마크에 성공했습니다.");
    }

    @Operation(summary = "숏츠 북마크 취소")
    @SwaggerApiError({ErrorStatus._EMPTY_SHORTS})
    @DeleteMapping("/shorts/{shortsId}/bookmarks")
    public ResponseEntity<String> deleteShortsBookmark(
        @CurrentUser User user,
        @PathVariable Long shortsId) {
        shortsBookmarkService.deleteShortsBookmark(user, shortsId);
        return ResponseEntity.ok("숏츠 북마크 취소에 성공했습니다.");
    }
}