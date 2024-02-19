package com.climeet.climeet_backend.domain.boardImage;

import com.climeet.climeet_backend.domain.board.Board;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardImageRepository extends JpaRepository<BoardImage, Long> {
    List<BoardImage> findAllByBoardId(Long boardId);
}