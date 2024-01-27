package com.climeet.climeet_backend.domain.shortslike;

import com.climeet.climeet_backend.domain.shorts.Shorts;
import com.climeet.climeet_backend.domain.shorts.ShortsRepository;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShortsLikeService {

    private final ShortsRepository shortsRepository;
    private final ShortsLikeRepository shortsLikeRepository;

    @Transactional
    public void createShortsLike(User user, Long shortsId) {
        Shorts shorts = shortsRepository.findById(shortsId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS));

        ShortsLike shortsLike = ShortsLike.toEntity(user, shorts);

        shortsLikeRepository.save(shortsLike);
    }

    @Transactional
    public void deleteShortsLike(User user, Long shortsId) {
        Shorts shorts = shortsRepository.findById(shortsId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS));

        shortsLikeRepository.deleteByUserAndShorts(user, shorts);
    }
}