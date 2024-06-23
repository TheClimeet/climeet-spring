package com.climeet.climeet_backend.domain.boardlike;

import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardLikeController {
    private final BoardLikeService boardLikeService;

    @PatchMapping("/boards/{boardId}/like")
    @SwaggerApiError({ErrorStatus._BOARD_NOT_FOUND, ErrorStatus._EXIST_BOARD_LIKE})
    @Operation(summary = "특정 공지사항 좋아요")
    public ResponseEntity<String> likeNotice(@CurrentUser User user, @PathVariable Long boardId){
        boardLikeService.boardLike(user, boardId);
        return ResponseEntity.ok("좋아요 완료");
    }

    @PatchMapping("/boards/{boardId}/unlike")
    @SwaggerApiError({ErrorStatus._BOARD_NOT_FOUND, ErrorStatus._UNEXIST_BOARD_LIKE})
    @Operation(summary = "특정 공지사항 좋아요 취소")
    public ResponseEntity<String> unLikeNotice(@CurrentUser User user, @PathVariable Long boardId){
        boardLikeService.boardUnLike(user, boardId);
        return ResponseEntity.ok("좋아요 취소 완료");
    }

}
