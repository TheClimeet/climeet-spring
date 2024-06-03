package com.climeet.climeet_backend.domain.climber.dto;

import com.climeet.climeet_backend.domain.climber.enums.ClimbingLevel;
import com.climeet.climeet_backend.domain.climber.enums.DiscoveryChannel;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
public class ClimberRequestDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateClimberRequest {

        private String token;
        private SocialType socialType;
        private String nickName;
        private ClimbingLevel climbingLevel;
        private DiscoveryChannel discoveryChannel;
        private String profileImgUrl;
        private List<Long> gymFollowList;
        private Boolean isAllowFollowNotification;
        private Boolean isAllowLikeNotification;
        private Boolean isAllowCommentNotification;
        private Boolean isAllowAdNotification;


    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClimberTokenRequest{
        private String accessToken;

    }


}
