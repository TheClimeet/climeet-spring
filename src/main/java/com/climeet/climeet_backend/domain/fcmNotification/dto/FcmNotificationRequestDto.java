package com.climeet.climeet_backend.domain.fcmNotification.dto;

import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class FcmNotificationRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreatePushNotificationRequest {

        private Long userId;
        private String title;
        private String message;

        @Builder
        public CreatePushNotificationRequest(Long userId, String title, String message){
            this.userId = userId;
            this.title = title;
            this.message = message;
        }

    }

    @Getter
    @NoArgsConstructor
    public static class CreateMultiplePushNotificationRequest{
        private List<Long> userIdList;
        private String title;
        private String message;

        @Builder
        public CreateMultiplePushNotificationRequest(List<Long> userIdList, String title, String message){
            this.userIdList = userIdList;
            this.title = title;
            this.message = message;
        }

    }

}
