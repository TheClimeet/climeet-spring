package com.climeet.climeet_backend.domain.shortscomment;

import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentRequestDto.CreateShortsCommentRequest;
import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentResponseDto.ShortsCommentChildResponse;
import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentResponseDto.ShortsCommentParentResponse;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "ShortsComment", description = "숏츠 댓글 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ShortsCommentController {

    private final ShortsCommentService shortsCommentService;

    @Operation(summary = "숏츠 댓글 작성")
    @SwaggerApiError({ErrorStatus._EMPTY_SHORTS, ErrorStatus._EMPTY_SHORTS_COMMENT})
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

    @Operation(summary = "숏츠 댓글 조회")
    @SwaggerApiError({ErrorStatus._EMPTY_SHORTS_COMMENT})
    @GetMapping("/shorts/{shortsId}")
    public ResponseEntity<PageResponseDto<List<ShortsCommentParentResponse>>> findShortsCommentList(
        @CurrentUser User user,
        @PathVariable Long shortsId,
        @RequestParam int page, @RequestParam int size
    ) {
        return ResponseEntity.ok(
            shortsCommentService.findShortsCommentList(user, shortsId, page, size));
    }

    @Operation(summary = "숏츠 대댓글 조회")
    @SwaggerApiError({ErrorStatus._EMPTY_SHORTS_COMMENT})
    @GetMapping("/shorts/{shortsId}/{parentCommentId}")
    public ResponseEntity<PageResponseDto<List<ShortsCommentChildResponse>>> findShortsChildCommentList(
        @CurrentUser User user,
        @PathVariable Long shortsId,
        @PathVariable Long parentCommentId,
        @RequestParam int page, @RequestParam int size
    ) {
        return ResponseEntity.ok(
            shortsCommentService.findShortsChildCommentList(user, shortsId, parentCommentId, page, size));
    }

    @Operation(summary = "숏츠 댓글 상호작용")
    @SwaggerApiError({ErrorStatus._EMPTY_SHORTS_COMMENT})
    @PatchMapping("/shortsComments/{shortsCommentId}")
    public ResponseEntity<String> changeShortsCommentLikeStatus(
        @CurrentUser User user,
        @PathVariable Long shortsCommentId,
        @RequestParam Boolean isLike, @RequestParam Boolean isDislike
    ) {
        shortsCommentService.changeShortsCommentLikeStatus(user, shortsCommentId, isLike,
            isDislike);
        return ResponseEntity.ok("숏츠 댓글 상호작용에 성공했습니다.");
    }
}