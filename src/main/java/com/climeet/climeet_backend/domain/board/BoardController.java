package com.climeet.climeet_backend.domain.board;

import com.climeet.climeet_backend.domain.board.dto.boardRequestDto.PostBoardRequest;
import com.climeet.climeet_backend.domain.board.dto.boardResponseDto.BoardDetailInfo;
import com.climeet.climeet_backend.domain.board.dto.boardResponseDto.BoardRetoolSimpleInfo;
import com.climeet.climeet_backend.domain.board.dto.boardResponseDto.BoardSimpleInfo;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    @PostMapping("/retool/boards")
    public ResponseEntity<String> createBoard(@RequestBody PostBoardRequest postBoardRequest) {
        boardService.createBoard(postBoardRequest);
        return ResponseEntity.ok("글 생성에 성공하였습니다.");
    }

    @GetMapping("/retool/boards")
    public ResponseEntity<List<BoardRetoolSimpleInfo>> findBoards() {
        return ResponseEntity.ok(boardService.findBoards());
    }

    @GetMapping("/boards")
    @SwaggerApiError(ErrorStatus._EMPTY_USER)
    @Operation(summary = "공지사항 전체 조회, 공지사항 글에 image없으면 null로 반환")
    public ResponseEntity<List<BoardSimpleInfo>> findBoardList(){
        return ResponseEntity.ok(boardService.findBoardList());
    }

    @GetMapping("/boards/{boardId}")
    @SwaggerApiError({ErrorStatus._EMPTY_USER, ErrorStatus._BOARD_NOT_FOUND})
    @Operation(summary = "특정 공지사항 조회")
    public ResponseEntity<BoardDetailInfo> findBoardById(@PathVariable Long boardId){
        return ResponseEntity.ok(boardService.findBoardById(boardId));
    }
}