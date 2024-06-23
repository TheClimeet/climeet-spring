package com.climeet.climeet_backend.domain.shortscomment;

import static com.climeet.climeet_backend.global.utils.DateTimeConverter.convertToDisplayTime;

import com.climeet.climeet_backend.domain.ShortsCommentLike.ShortsCommentLike;
import com.climeet.climeet_backend.domain.ShortsCommentLike.ShortsCommentLikeRepository;
import com.climeet.climeet_backend.domain.ShortsCommentLike.ShortsCommentLikeService;
import com.climeet.climeet_backend.domain.fcmNotification.FcmNotificationService;
import com.climeet.climeet_backend.domain.fcmNotification.NotificationType;
import com.climeet.climeet_backend.domain.shorts.Shorts;
import com.climeet.climeet_backend.domain.shorts.ShortsRepository;
import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentRequestDto.CreateShortsCommentRequest;
import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentResponseDto.ShortsCommentChildResponse;
import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentResponseDto.ShortsCommentParentResponse;
import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentResponseDto.ShortsCommentResponse;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.google.firebase.messaging.FirebaseMessagingException;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
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
    private final ShortsCommentLikeRepository shortsCommentLikeRepository;
    private final FcmNotificationService fcmNotificationService;
    private static final int ADJUSTED_CHILD_COUNT = 1;

    @Transactional
    public ShortsCommentParentResponse createShortsComment(User user, Long shortsId,
        CreateShortsCommentRequest createShortsCommentRequest, Long parentCommentId,
        boolean isReply) throws FirebaseMessagingException {

        Shorts shorts = shortsRepository.findById(shortsId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS));

        shorts.updateViewCountUp();
        shorts.updateCommentCount();

        ShortsComment shortsComment = ShortsComment.toEntity(user, createShortsCommentRequest,
            shorts);



        if (isReply) {
            ShortsComment parentComment = shortsCommentRepository.findById(parentCommentId)
                .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS_COMMENT));

            if(parentComment.getChildCommentCount() != 0) {
                shortsComment.updateIsFirstChildFalse();
            }
            parentComment.updateChildCommentCount();
            shortsComment.updateParentComment(parentComment);
            shortsComment.updateIsParentFalse();
            List<Long> userList = new LinkedList<>();
            userList.add(shorts.getUser().getId());
            userList.add(parentComment.getShorts().getUser().getId());
            fcmNotificationService.sendMultipleUser(userList, NotificationType.CHILD_COMMENT_MY_SHORTS.getTitle(user.getProfileName()), shortsComment.getContent());

        }

        shortsCommentRepository.save(shortsComment);

        if(!isReply) {
            fcmNotificationService.sendSingleUser(shorts.getUser().getId(),
                NotificationType.PARENT_COMMENT_MY_SHORTS.getTitle(user.getProfileName()),
                shortsComment.getContent());
        }

        return ShortsCommentParentResponse.toDTO(
            shortsComment.getUser(), shortsComment,
            CommentLikeStatus.NONE,
            fetchParentCommentId(shortsComment),
            shortsComment.getChildCommentCount() - ADJUSTED_CHILD_COUNT,
            convertToDisplayTime(shortsComment.getCreatedAt())
        );


    }

    public PageResponseDto<List<ShortsCommentParentResponse>> findShortsCommentList(User user,
        Long shortsId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<ShortsComment> parentCommentList = shortsCommentRepository.findParentCommentsByShortsIdOrderedByCreatedAtDesc(
                shortsId, pageable)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS_COMMENT));

        List<ShortsComment> shortsCommentIncludeChildList = fetchParentAndFirstChildComments(
            parentCommentList.getContent());

        Map<Long, CommentLikeStatus> likeStatusMap = shortsCommentLikeService.fetchUserLikeStatuses(
            user, shortsCommentIncludeChildList);

        List<ShortsCommentParentResponse> responses = shortsCommentIncludeChildList.stream()
            .map(comment -> ShortsCommentParentResponse.toDTO(
                comment.getUser(), comment,
                likeStatusMap.getOrDefault(comment.getId(), CommentLikeStatus.NONE),
                fetchParentCommentId(comment),
                comment.getChildCommentCount() - ADJUSTED_CHILD_COUNT,
                convertToDisplayTime(comment.getCreatedAt())
            ))
            .collect(Collectors.toList());

        return new PageResponseDto<>(pageable.getPageNumber(), parentCommentList.hasNext(),
            responses);
    }

    public PageResponseDto<List<ShortsCommentChildResponse>> findShortsChildCommentList(User user,
        Long shortsId, Long parentCommentId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<ShortsComment> childCommentList = shortsCommentRepository.findChildCommentsByShortsIdAndParentCommentIdAndIsFirstChildFalseOrderByCreatedAtDesc(
                shortsId, parentCommentId, pageable)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS_COMMENT));

        Map<Long, CommentLikeStatus> likeStatusMap = shortsCommentLikeService.fetchUserLikeStatuses(
            user, childCommentList.getContent());

        List<ShortsCommentChildResponse> responses = childCommentList.stream()
            .map(comment -> ShortsCommentChildResponse.toDTO(
                comment.getId(),
                comment.getUser().getProfileName(),
                comment.getUser().getProfileImageUrl(),
                comment.getContent(),
                likeStatusMap.getOrDefault(comment.getId(), CommentLikeStatus.NONE),
                comment.getLikeCount(),
                comment.getDislikeCount(),
                fetchParentCommentId(comment),
                convertToDisplayTime(comment.getCreatedAt())
            ))
            .collect(Collectors.toList());

        return new PageResponseDto<>(pageable.getPageNumber(), childCommentList.hasNext(),
            responses);
    }

    @Transactional
    public CommentLikeStatus changeShortsCommentLikeStatus(User user, Long shortsCommentId,
        boolean isLike,
        boolean isDislike) {
        ShortsComment shortsComment = shortsCommentRepository.findById(shortsCommentId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS_COMMENT));

        CommentLikeStatus commentLikeStatus = determineCommentLikeStatus(isLike, isDislike);

        Optional<ShortsCommentLike> optionalShortsCommentLike = shortsCommentLikeRepository.findShortsCommentLikeByUserAndShortsComment(
            user, shortsComment);

        if (optionalShortsCommentLike.isPresent()) {
            ShortsCommentLike existingShortsCommentLike = optionalShortsCommentLike.get();
            shortsCommentLikeService.updateCountsBasedOnStatusChange(existingShortsCommentLike,
                commentLikeStatus, shortsComment);
            optionalShortsCommentLike.get().updateCommentLikeStatus(commentLikeStatus);
        } else {
            ShortsCommentLike newShortsCommentLike = ShortsCommentLike.toEntity(user, shortsComment,
                commentLikeStatus);
            shortsCommentLikeService.updateCountsForNewLike(newShortsCommentLike, shortsComment);
            shortsCommentLikeRepository.save(newShortsCommentLike);
        }

        return commentLikeStatus;
    }

    private List<ShortsComment> fetchParentAndFirstChildComments(
        List<ShortsComment> parentComments) {
        List<ShortsComment> commentListWithChild = new ArrayList<>();

        parentComments.forEach(parentComment -> {
            commentListWithChild.add(parentComment);
            // 댓글의 가장 오래된 대댓글 가져와 부모 댓글 바로 뒤에 추가
            shortsCommentRepository.findFirstChildCommentByIdAndNotParentOrderByCreatedAtAsc(
                    parentComment.getId())
                .ifPresent(commentListWithChild::add);
        });

        return commentListWithChild;
    }

    //내가 작성한 댓글 가져오기 - 최신순
    public PageResponseDto<List<ShortsCommentResponse>> findMyShortsComments(User user, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<ShortsComment> commentSlice = shortsCommentRepository.findByUserOrderByCreatedAtDesc(user, pageable);

        List<ShortsCommentResponse> responseList = commentSlice.stream()
            .map(comment -> ShortsCommentResponse.builder()
                .shortsId(comment.getShorts().getId())
                .commentId(comment.getId())
                .profileImageUrl(comment.getUser().getProfileImageUrl())
                .content(comment.getContent())
                .createdDate(convertToDisplayTime(comment.getCreatedAt()))
                .build()
            ).toList();

        return new PageResponseDto<>(pageable.getPageNumber(), commentSlice.hasNext(), responseList);
    }

    private Long fetchParentCommentId(ShortsComment comment) {
        if (comment.getParentComment() != null) {
            return comment.getParentComment().getId();
        } else {
            return null;
        }
    }

    private CommentLikeStatus determineCommentLikeStatus(Boolean isLike, Boolean isDislike) {
        if (Boolean.TRUE.equals(isLike)) {
            return CommentLikeStatus.LIKE;
        } else if (Boolean.TRUE.equals(isDislike)) {
            return CommentLikeStatus.DISLIKE;
        } else {
            return CommentLikeStatus.NONE;
        }
    }


}