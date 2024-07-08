package com.climeet.climeet_backend.domain.blockeduser;

import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BlockUserService {
    private final BlockedUserRepository blockedUserRepository;

    @Transactional
    public void blockUser(User blocker, User blocked) {
        if (blockedUserRepository.existsByBlockerAndBlocked(blocker, blocked)) {
            throw new GeneralException(ErrorStatus._ALREADY_BLOCKED);
        }

        BlockedUser blockedUser = BlockedUser.builder()
            .blocker(blocker)
            .blocked(blocked)
            .build();

        blockedUserRepository.save(blockedUser);
    }
}