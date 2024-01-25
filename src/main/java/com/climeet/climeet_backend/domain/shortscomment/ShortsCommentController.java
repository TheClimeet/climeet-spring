package com.climeet.climeet_backend.domain.shortscomment;

import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentRequestDto.CreateShortsCommentRequest;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "ShortsComment", description = "숏츠 댓글 API")
@RequiredArgsConstructor
@RestController
public class ShortsCommentController {

    private final ShortsCommentService shortsCommentService;

    @Operation(summary = "숏츠 댓글 작성")
    @SwaggerApiError({ErrorStatus._EMPTY_SHORTS})
    @PostMapping("/shorts/{shortsId}/shortsComments")
    public ResponseEntity<String> createShortsComment(
        @CurrentUser User user,
        @PathVariable Long shortsId,
        @RequestBody CreateShortsCommentRequest createShortsCommentRequest,
        @RequestParam(required = false) Long parentCommentId) {
        shortsCommentService.createShortsComment(user, shortsId, createShortsCommentRequest,
            parentCommentId, parentCommentId != null);
        return ResponseEntity.ok("댓글 작성에 성공했습니다.");
    }
}