package com.climeet.climeet_backend.domain.user.dto;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserRequestDto {

    @Getter
    @NoArgsConstructor
    public static class UpdateUserAllowNotificationRequest {
        private Boolean isAllowFollowNotification;
        private Boolean isAllowLikeNotification;
        private Boolean isAllowCommentNotification;
        private Boolean isAllowAdNotification;
    }

    @Getter
    public static class UpdateUserFcmToken{
        private String fcmToken;
    }

    public static class UpdateUserProfile{

    }

}
