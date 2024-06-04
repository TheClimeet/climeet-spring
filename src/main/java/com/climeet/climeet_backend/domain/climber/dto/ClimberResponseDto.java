package com.climeet.climeet_backend.domain.climber.dto;

import com.climeet.climeet_backend.domain.climber.Climber;
import com.climeet.climeet_backend.domain.climber.enums.ResponseType;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClimberResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class LoginSimpleInfo {

        private SocialType socialType;
        private String accessToken;
        private String refreshToken;
        private ResponseType responseType;


        public static LoginSimpleInfo toDTO(String socialType, String accessToken, String refreshToken, ResponseType responseType) {
            return LoginSimpleInfo.builder()
                .socialType(SocialType.valueOf(socialType))
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .responseType(responseType)
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

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClimberPrivacySettingInfo {

        private boolean isShortsPublic;
        private boolean isHomeGymPublic;
        private boolean isAverageCompletionRatePublic;
        private boolean isAverageCompletionLevelPublic;

        public static ClimberPrivacySettingInfo toDTO(Climber climber){
            return ClimberPrivacySettingInfo.builder()
                .isShortsPublic(climber.isShortsPublic())
                .isHomeGymPublic(climber.isHomeGymPublic())
                .isAverageCompletionRatePublic(climber.isAverageCompletionRatePublic())
                .isAverageCompletionLevelPublic(climber.isAverageCompletionLevelPublic())
                .build();
        }

    }

}


