package com.climeet.climeet_backend.domain.user.dto;

import com.climeet.climeet_backend.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserShortsSimpleInfo{
        private Long userId;
        private String profileImgUrl;
        private String profileName;

        public static UserShortsSimpleInfo toDto(User user) {
            return UserShortsSimpleInfo.builder()
                .profileImgUrl(user.getProfileImageUrl())
                .profileName(user.getProfileName())
                .userId(user.getId())
                .build();
        }
    }
}
