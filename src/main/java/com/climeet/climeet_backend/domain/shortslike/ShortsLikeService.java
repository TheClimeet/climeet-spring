package com.climeet.climeet_backend.domain.shortslike;

import com.climeet.climeet_backend.domain.fcmNotification.FcmNotificationService;
import com.climeet.climeet_backend.domain.fcmNotification.NotificationType;
import com.climeet.climeet_backend.domain.shorts.Shorts;
import com.climeet.climeet_backend.domain.shorts.ShortsRepository;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.google.firebase.messaging.FirebaseMessagingException;
import jakarta.transaction.Transactional;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShortsLikeService {

    private final ShortsRepository shortsRepository;
    private final ShortsLikeRepository shortsLikeRepository;
    private final FcmNotificationService fcmNotificationService;

    @Transactional
    public void changeShortsLikeStatus(User user, Long shortsId) throws FirebaseMessagingException {
        Shorts shorts = shortsRepository.findById(shortsId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS));

        Optional<ShortsLike> optionalShortsLike = shortsLikeRepository.findByUserAndShorts(user,
            shorts);

        if(optionalShortsLike.isEmpty()) {
            ShortsLike shortsLike = ShortsLike.toEntity(user, shorts);
            shortsLikeRepository.save(shortsLike);
        }
        if(optionalShortsLike.isPresent()) {
            ShortsLike shortsLike = optionalShortsLike.get();
            shortsLike.switchLikeStatus();
        }
        fcmNotificationService.sendSingleUser(shorts.getUser().getId(), NotificationType.HEARTS_MY_SHORTS_WITHOUT_COUNT.getTitle(user.getProfileName()), NotificationType.HEARTS_MY_SHORTS_WITHOUT_COUNT.getMessage());

    }
}