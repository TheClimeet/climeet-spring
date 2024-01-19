package com.climeet.climeet_backend.domain.shortscomment;

import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentRequestDto.CreateShortsCommentRequest;
import com.climeet.climeet_backend.global.response.ApiResponse;
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

    /**
     * [POST] 숏츠 댓글 작성 (commentId가 null일 경우 대댓글 작성)
     */
    @Operation(summary = "숏츠 댓글 작성")
    @PostMapping("/shorts/{shortsId}/shortsComments")
    public ApiResponse<String> createShortsComment(
        @PathVariable Long shortsId,
        @RequestBody CreateShortsCommentRequest createShortsCommentRequest,
        @RequestParam(required = false) Long parentCommentId) {
        shortsCommentService.createShortsComment(shortsId, createShortsCommentRequest,
            parentCommentId, parentCommentId != null);
        return ApiResponse.onSuccess("댓글 작성에 성공했습니다.");
    }
}