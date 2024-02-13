package com.climeet.climeet_backend.domain.climber.dto;

import com.climeet.climeet_backend.domain.climber.enums.ClimbingLevel;
import com.climeet.climeet_backend.domain.climber.enums.DiscoveryChannel;
import java.util.List;
import lombok.Data;
import lombok.Getter;

@Data
public class ClimberRequestDto {
    @Getter
    public static class CreateClimberRequest {

        private String nickName;
        private ClimbingLevel climbingLevel;
        private DiscoveryChannel discoveryChannel;
        private String profileImgUrl;
        private List<Long> gymFollowList;
        private Boolean isAllowFollowNotification;
        private Boolean isAllowLikeNotification;
        private Boolean isAllowCommentNotification;
        private Boolean isAllowAdNotification;
        private String fcmToken;


    }


}
