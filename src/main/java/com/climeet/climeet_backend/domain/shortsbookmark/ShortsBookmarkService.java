package com.climeet.climeet_backend.domain.shortsbookmark;

import com.climeet.climeet_backend.domain.shorts.Shorts;
import com.climeet.climeet_backend.domain.shorts.ShortsRepository;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShortsBookmarkService {

    private final ShortsRepository shortsRepository;
    private final ShortsBookmarkRepository shortsBookmarkRepository;

    @Transactional
    public void changeShortsBookmarkStatus(User user, Long shortsId) {
        Shorts shorts = shortsRepository.findById(shortsId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS));

        Optional<ShortsBookmark> optionalShortsBookmark = shortsBookmarkRepository.findByUserAndShorts(
            user, shorts);

        if (optionalShortsBookmark.isEmpty()) {
            ShortsBookmark shortsBookmark = ShortsBookmark.toEntity(user, shorts);
            shortsBookmarkRepository.save(shortsBookmark);
        }
        if (optionalShortsBookmark.isPresent()) {
            ShortsBookmark shortsBookmark = optionalShortsBookmark.get();
            shortsBookmark.switchBookmarkStatus();
        }
    }
}