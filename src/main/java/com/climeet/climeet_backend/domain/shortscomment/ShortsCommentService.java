package com.climeet.climeet_backend.domain.shortscomment;

import static com.climeet.climeet_backend.global.utils.DateTimeConverter.convertToDisplayTime;

import com.climeet.climeet_backend.domain.ShortsCommentLike.ShortsCommentLikeService;
import com.climeet.climeet_backend.domain.shorts.Shorts;
import com.climeet.climeet_backend.domain.shorts.ShortsRepository;
import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentRequestDto.CreateShortsCommentRequest;
import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentResponseDto.ShortsCommentResponse;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShortsCommentService {

    private final ShortsRepository shortsRepository;
    private final ShortsCommentRepository shortsCommentRepository;
    private final ShortsCommentLikeService shortsCommentLikeService;
    private static final int ADJUSTED_CHILD_COUNT = 1;

    @Transactional
    public void createShortsComment(User user, Long shortsId,
        CreateShortsCommentRequest createShortsCommentRequest, Long parentCommentId,
        boolean isReply) {

        Shorts shorts = shortsRepository.findById(shortsId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS));

        ShortsComment shortsComment = ShortsComment.toEntity(user, createShortsCommentRequest,
            shorts);

        if (isReply) {
            ShortsComment parentComment = shortsCommentRepository.findById(parentCommentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._Empty_SHORTS_COMMENT));
            parentComment.updateChildCommentCount();
            shortsComment.updateParentComment(parentComment);
        }

        shortsCommentRepository.save(shortsComment);
    }

    public PageResponseDto<List<ShortsCommentResponse>> findShortsCommentList(User user,
        Long shortsId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<ShortsComment> parentCommentList = shortsCommentRepository.findParentCommentsByShortsIdOrderedByCreatedAtAsc(
                shortsId, pageable)
            .orElseThrow(() -> new GeneralException(ErrorStatus._Empty_SHORTS_COMMENT));

        List<ShortsComment> shortsCommentIncludeChildList = fetchParentAndFirstChildComments(
            parentCommentList.getContent());

        Map<Long, CommentLikeStatus> likeStatusMap = shortsCommentLikeService.fetchUserLikeStatuses(
            user, shortsCommentIncludeChildList);

        List<ShortsCommentResponse> responses = shortsCommentIncludeChildList.stream()
            .map(comment -> ShortsCommentResponse.toDto(
                comment.getId(),
                comment.getUser().getProfileName(),
                comment.getUser().getProfileImageUrl(),
                comment.getContent(),
                likeStatusMap.getOrDefault(comment.getId(), CommentLikeStatus.NONE),
                comment.getChildCommentCount() != 0,
                comment.getLikeCount(),
                comment.getDislikeCount(),
                fetchParentCommentId(comment),
                comment.getChildCommentCount() - ADJUSTED_CHILD_COUNT,
                convertToDisplayTime(comment.getCreatedAt())
            ))
            .collect(Collectors.toList());

        return new PageResponseDto<>(pageable.getPageNumber(), parentCommentList.hasNext(),
            responses);
    }

    private List<ShortsComment> fetchParentAndFirstChildComments(
        List<ShortsComment> parentComments) {
        List<ShortsComment> commentListWithChild = new ArrayList<>();

        parentComments.forEach(parentComment -> {
            commentListWithChild.add(parentComment);
            // 댓글의 가장 오래된 대댓글 가져와 부모 댓글 바로 뒤에 추가
            shortsCommentRepository.findFirstChildCommentByIdAndNotParentOrderByCreatedAtAsc(
                    parentComment.getId())
                .ifPresent(childComment -> commentListWithChild.add((ShortsComment) childComment));
        });

        return commentListWithChild;
    }

    private Long fetchParentCommentId(ShortsComment comment) {
        if (comment.getParentComment() != null) {
            return comment.getParentComment().getId();
        } else {
            return null;
        }
    }

}