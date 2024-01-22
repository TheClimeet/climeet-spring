package com.climeet.climeet_backend.domain.user;

import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    public User updateNotification(User user, boolean isAllowFollowNotification, boolean isAllowLikeNotification, boolean isAllowCommentNotification, boolean isAllowAdNotification){
        if(user==null){
            throw new GeneralException(ErrorStatus._EMPTY_MEMBER);
        }
        user.updateNotification(isAllowFollowNotification, isAllowLikeNotification, isAllowCommentNotification, isAllowAdNotification);
        return userRepository.save(user);

    }

}