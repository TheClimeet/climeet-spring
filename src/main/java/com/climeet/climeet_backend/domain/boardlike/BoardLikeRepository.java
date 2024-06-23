package com.climeet.climeet_backend.domain.boardlike;

import com.climeet.climeet_backend.domain.board.Board;
import com.climeet.climeet_backend.domain.user.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike, Long> {
    Optional<BoardLike> findByUserAndBoard(User user, Board board);
}
