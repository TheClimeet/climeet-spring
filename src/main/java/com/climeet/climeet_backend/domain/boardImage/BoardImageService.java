package com.climeet.climeet_backend.domain.boardImage;

import com.climeet.climeet_backend.domain.board.Board;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardImageService {

    private final BoardImageRepository boardImageRepository;


    public void createBoardImages(Board board, List<String> imgUrls) {
        for(String url : imgUrls) {
            BoardImage boardImage = BoardImage.toEntity(url, board);
            boardImageRepository.save(boardImage);
        }
    }
}