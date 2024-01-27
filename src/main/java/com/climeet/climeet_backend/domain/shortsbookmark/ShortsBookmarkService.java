package com.climeet.climeet_backend.domain.shortsbookmark;

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
public class ShortsBookmarkService {

    private final ShortsRepository shortsRepository;
    private final ShortsBookmarkRepository shortsBookmarkRepository;

    @Transactional
    public void changeShortsBookmarked(User user, Long shortsId) {
        Shorts shorts = shortsRepository.findById(shortsId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS));

        System.out.println(user.getAccessToken());
        ShortsBookmark shortsBookmark = ShortsBookmark.toEntity(user, shorts);

        shortsBookmarkRepository.save(shortsBookmark);
    }

    @Transactional
    public void changeShortsUnBookmarked(User user, Long shortsId) {
        Shorts shorts = shortsRepository.findById(shortsId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS));

        shortsBookmarkRepository.deleteByUserAndShorts(user, shorts);
    }
}