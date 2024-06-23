package com.climeet.climeet_backend.domain.shortsbookmark;

import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
@Tag(name = "ShortsBookmark")

public class ShortsBookmarkController {

    private final ShortsBookmarkService shortsBookmarkService;

    @Operation(summary = "숏츠 북마크 - 2501 [진로]")
    @SwaggerApiError({ErrorStatus._EMPTY_SHORTS})
    @PatchMapping("/shorts/{shortsId}/bookmarks")
    public ResponseEntity<String> createShortsBookmark(
        @CurrentUser User user,
        @PathVariable Long shortsId) {
        shortsBookmarkService.changeShortsBookmarkStatus(user, shortsId);
        return ResponseEntity.ok("숏츠 북마크 생성/취소 성공했습니다.");
    }
}