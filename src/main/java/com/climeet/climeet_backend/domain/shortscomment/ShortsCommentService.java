package com.climeet.climeet_backend.domain.shortscomment;

import com.climeet.climeet_backend.domain.shorts.Shorts;
import com.climeet.climeet_backend.domain.shorts.ShortsRepository;
import com.climeet.climeet_backend.domain.shortscomment.dto.ShortsCommentRequestDto.CreateShortsCommentRequest;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShortsCommentService {

    private final ShortsRepository shortsRepository;
    private final ShortsCommentRepository shortsCommentRepository;

    @Transactional
    public void createShortsComment(Long shortsId,
        CreateShortsCommentRequest createShortsCommentRequest, Long parentCommentId,
        boolean isReply) {

        Shorts shorts = shortsRepository.findById(shortsId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS));

        ShortsComment shortsComment = ShortsComment.toEntity(createShortsCommentRequest, shorts);
        if (isReply) {
            shortsComment.updateParentCommentId(parentCommentId);
        }

        shortsCommentRepository.save(shortsComment);
    }
}