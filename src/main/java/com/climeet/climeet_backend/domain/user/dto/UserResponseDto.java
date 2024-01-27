package com.climeet.climeet_backend.domain.user.dto;

import com.climeet.climeet_backend.domain.user.User;
import lombok.Data;
import lombok.Getter;

public class UserResponseDto {
    @Data
    @Getter
    public static class UserTokenSimpleInfo{
        private String accessToken;
        private String refreshToken;

        public UserTokenSimpleInfo(User user){
            this.accessToken = user.getAccessToken();
            this.refreshToken = user.getRefreshToken();
        }

    }

}
