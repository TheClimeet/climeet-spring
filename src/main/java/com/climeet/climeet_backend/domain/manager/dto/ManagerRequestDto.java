package com.climeet.climeet_backend.domain.manager.dto;

import com.climeet.climeet_backend.domain.climbinggym.enums.ServiceBitmask;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ManagerRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateManagerRequest {

        private Long gymId;
        private String loginId;
        private String password;
        private String name;
        private String phoneNumber;
        private String email;
        private String backGroundImageUri;
        private List<ServiceBitmask> provideServiceList;
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
