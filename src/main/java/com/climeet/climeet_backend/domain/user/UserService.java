package com.climeet.climeet_backend.domain.user;

import com.climeet.climeet_backend.domain.climber.ClimberRepository;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserTokenSimpleInfo;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.security.JwtTokenProvider;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final ClimberRepository climberRepository;
    public User updateNotification(User user, boolean isAllowFollowNotification, boolean isAllowLikeNotification, boolean isAllowCommentNotification, boolean isAllowAdNotification){
        if(user==null){
            throw new GeneralException(ErrorStatus._EMPTY_MEMBER);
        }
        user.updateNotification(isAllowFollowNotification, isAllowLikeNotification, isAllowCommentNotification, isAllowAdNotification);
        return userRepository.save(user);

    }
    public UserTokenSimpleInfo updateUserToken(String refreshToken){
        Long userId = Long.valueOf(jwtTokenProvider.getPayload(refreshToken));
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new GeneralException(ErrorStatus._INVALID_JWT));
        String newRefreshToken = jwtTokenProvider.createRefreshToken(userId);
        String newAccesstoken = jwtTokenProvider.refreshAccessToken(refreshToken);
        user.updateToken(newRefreshToken, newAccesstoken);

        return new UserTokenSimpleInfo(user);

    }
//eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxNCIsImlhdCI6MTcwNjY4MDgzMiwiZXhwIjoxNzA2NjgxNDM3fQ.1HFtfxeqiLTinrt0SSrQJaPJt5N5_H8mPwTgxwG4wY8ItrnkaMifOrjz5znqGar1b-jNzXI5sKqp-V0hCcG5KQ
}