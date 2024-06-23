package com.climeet.climeet_backend.domain.boardlike;

import com.climeet.climeet_backend.domain.board.Board;
import com.climeet.climeet_backend.domain.board.BoardRepository;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BoardLikeService {
    private final BoardLikeRepository boardLikeRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public void boardLike(User user, Long boardId){
        Board board = boardRepository.findById(boardId)
            .orElseThrow(()-> new GeneralException(ErrorStatus._BOARD_NOT_FOUND));
        if(boardLikeRepository.findByUserAndBoard(user, board).isPresent()){
            throw new GeneralException(ErrorStatus._EXIST_BOARD_LIKE);
        }
        BoardLike boardLike = BoardLike.toEntity(user, board);
        boardLikeRepository.save(boardLike);
    }

    @Transactional
    public void boardUnLike(User user, Long boardId){
        Board board = boardRepository.findById(boardId)
            .orElseThrow(()-> new GeneralException(ErrorStatus._BOARD_NOT_FOUND));
        BoardLike boardLike = boardLikeRepository.findByUserAndBoard(user, board)
                .orElseThrow(()-> new GeneralException(ErrorStatus._UNEXIST_BOARD_LIKE));
        boardLikeRepository.delete(boardLike);
    }

}
