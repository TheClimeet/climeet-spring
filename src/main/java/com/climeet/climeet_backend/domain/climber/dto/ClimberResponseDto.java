package com.climeet.climeet_backend.domain.climber.dto;

import com.climeet.climeet_backend.domain.climber.Climber;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClimberResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClimberSimpleInfo {

        private SocialType socialType;
        private String accessToken;
        private String refreshToken;


        public static ClimberSimpleInfo toDTO(Climber climber) {
            return ClimberSimpleInfo.builder()
                .socialType(climber.getSocialType())
                .accessToken(climber.getAccessToken())
                .refreshToken(climber.getRefreshToken())
                .build();
        }

        @Getter
        public static class ClimberTokenRefreshResponse {

            @JsonProperty("access_token")
            private String accessToken;
        }

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClimberDetailInfo {

        private Long userId;
        private String climberName;
        private String profileImgUrl;
        private Long followerCount;
        private Boolean isFollower;

        public static ClimberDetailInfo toDTO(Climber climber, boolean status){
            return ClimberDetailInfo.builder()
                .userId(climber.getId())
                .climberName(climber.getProfileName())
                .profileImgUrl(climber.getProfileImageUrl())
                .followerCount(climber.getFollowerCount())
                .isFollower(status)
                .build();
        }


    }

}


