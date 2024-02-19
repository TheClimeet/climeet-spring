package com.climeet.climeet_backend.domain.FcmMessage.dto;

import com.climeet.climeet_backend.domain.FcmMessage.FcmMessage.Message;
import com.climeet.climeet_backend.domain.user.User;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FCMNotificationRequestDto {

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
