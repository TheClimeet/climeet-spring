package com.climeet.climeet_backend.domain.board;

import com.climeet.climeet_backend.domain.board.dto.boardRequestDto.PostBoardRequest;
import com.climeet.climeet_backend.domain.board.dto.boardResponseDto.BoardDetailInfo;
import com.climeet.climeet_backend.domain.board.dto.boardResponseDto.BoardRetoolSimpleInfo;
import com.climeet.climeet_backend.domain.board.dto.boardResponseDto.BoardSimpleInfo;
import com.climeet.climeet_backend.domain.boardImage.BoardImage;
import com.climeet.climeet_backend.domain.boardImage.BoardImageRepository;
import com.climeet.climeet_backend.domain.boardImage.BoardImageService;
import com.climeet.climeet_backend.domain.boardlike.BoardLike;
import com.climeet.climeet_backend.domain.boardlike.BoardLikeRepository;
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
    private final BoardImageRepository boardImageRepository;
    private final BoardLikeRepository boardLikeRepository;
    public static final Long CLIMEET_OFFICIAL_ACCOUNT = 1L;


    @Transactional
    public void createBoard(PostBoardRequest postBoardRequest) {

        User user = userRepository.findById(postBoardRequest.getAdminId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MEMBER));
        Board board = Board.toEntity(user, postBoardRequest.getTitle(),
            postBoardRequest.getContent(), BoardType.NOTICE);
        boardRepository.save(board);

        boardImageService.createBoardImages(board, postBoardRequest.getImgUrls());

    }

    public List<BoardRetoolSimpleInfo> findBoards() {

        List<Board> boardList = boardRepository.findAll();

        return boardList.stream().map(BoardRetoolSimpleInfo::toDTO)
            .toList();
    }

    public List<BoardSimpleInfo> findBoardList() {
        User climeetOfficialAccount = userRepository.findById(CLIMEET_OFFICIAL_ACCOUNT)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_USER));

        List<Board> boardList = boardRepository.findAll();
        return boardList.stream()
            .map(board -> {
                List<BoardImage> boardImageList = boardImageRepository.findAllByBoardId(board.getId());
                if (boardImageList.isEmpty()) {
                    return BoardSimpleInfo.toDTO(board, null, climeetOfficialAccount);
                } else {
                    return BoardSimpleInfo.toDTO(board, boardImageList.get(0).getImageUrl(), climeetOfficialAccount);
                }
            })
            .toList();
    }

    public BoardDetailInfo findBoardById(Long boardId, User user) {
        User climeetOfficialAccount = userRepository.findById(CLIMEET_OFFICIAL_ACCOUNT)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_USER));
        Board board = boardRepository.findById(boardId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._BOARD_NOT_FOUND));
        boolean likeStatus = false;
        if(boardLikeRepository.findByUserAndBoard(user, board).isPresent()){
            likeStatus = true;
        }
        return BoardDetailInfo.toDTO(board, boardImageRepository
            .findAllByBoardId(boardId).stream().map(BoardImage::getImageUrl).toList(),
            climeetOfficialAccount, likeStatus);
    }
}