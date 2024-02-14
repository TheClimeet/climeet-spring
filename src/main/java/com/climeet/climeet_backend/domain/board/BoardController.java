package com.climeet.climeet_backend.domain.board;

import com.climeet.climeet_backend.domain.board.dto.boardRequestDto.PostBoardRequest;
import com.climeet.climeet_backend.domain.board.dto.boardResponseDto.BoardSimpleInfo;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
    public ResponseEntity<List<BoardSimpleInfo>> findBoards() {
        return ResponseEntity.ok(boardService.findBoards());
    }
}