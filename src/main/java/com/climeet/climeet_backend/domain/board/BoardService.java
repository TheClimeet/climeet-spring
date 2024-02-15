package com.climeet.climeet_backend.domain.board;

import com.climeet.climeet_backend.domain.board.dto.boardRequestDto.PostBoardRequest;
import com.climeet.climeet_backend.domain.board.dto.boardResponseDto.BoardSimpleInfo;
import com.climeet.climeet_backend.domain.boardImage.BoardImageService;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.UserRepository;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final BoardImageService boardImageService;

    @Transactional
    public void createBoard(PostBoardRequest postBoardRequest) {

        User user = userRepository.findById(postBoardRequest.getAdminId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MEMBER));
        Board board = Board.toEntity(user, postBoardRequest.getTitle(),
            postBoardRequest.getContent(), BoardType.NOTICE);
        boardRepository.save(board);

        boardImageService.createBoardImages(board, postBoardRequest.getImgUrls());

    }

    public List<BoardSimpleInfo> findBoards() {

        List<Board> boardList = boardRepository.findAll();

        return boardList.stream().map(BoardSimpleInfo::toDTO)
            .toList();
    }
}