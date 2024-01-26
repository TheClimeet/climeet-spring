package com.climeet.climeet_backend.domain.ShortsCommentLike;

import com.climeet.climeet_backend.domain.shortscomment.CommentLikeStatus;
import com.climeet.climeet_backend.domain.shortscomment.ShortsComment;
import com.climeet.climeet_backend.domain.user.User;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ShortsCommentLikeService {

    private final ShortsCommentLikeRepository shortsCommentLikeRepository;

    private static final int COMMENT_INDEX = 0;
    private static final int LIKE_STATUS_INDEX = 1;
    public Map<Long, CommentLikeStatus> fetchUserLikeStatuses(
        User user, List<ShortsComment> comments) {
        List<Object[]> likeStatusList = shortsCommentLikeRepository.findCommentLikeStatusByUserAndCommentsIn(user, comments);

        return likeStatusList.stream()
            .collect(Collectors.toMap(
                result -> (Long) result[COMMENT_INDEX], // 댓글 ID
                result -> (CommentLikeStatus) result[LIKE_STATUS_INDEX] // 좋아요/싫어요 상태
            ));
    }
}