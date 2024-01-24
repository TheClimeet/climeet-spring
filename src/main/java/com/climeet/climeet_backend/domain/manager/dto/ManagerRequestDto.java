package com.climeet.climeet_backend.domain.manager.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ManagerRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateManagerRequest {

        private String gymName;
        private String loginId;
        private String password;
        private String name;
        private String phoneNumber;
        private String email;
        private String backGroundImageUri;
        private List<String> provideServiceList; //구현 후 처리 예정
        private String businessRegistrationImageUrl;
        private Boolean isAllowFollowNotification;
        private Boolean isAllowLikeNotification;
        private Boolean isAllowCommentNotification;
        private Boolean isAllowAdNotification;
    }

    @Getter
    @NoArgsConstructor
    public static class CreateAccessTokenRequest{
        private String loginId;
        private String password;
    }


}
