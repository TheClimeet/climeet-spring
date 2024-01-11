package com.climeet.climeet_backend.domain.climber.dto;

import com.climeet.climeet_backend.domain.climber.enums.ClimbingLevel;
import com.climeet.climeet_backend.domain.climber.enums.DiscoveryChannel;
import lombok.Data;
import lombok.Getter;

@Data
public class ClimberSignUpRequestDto {
    private String nickName;
    private ClimbingLevel climbingLevel;
    private DiscoveryChannel discoveryChannel;
//    private Boolean isAllowFollowNotification;
//    private Boolean isAllowLikeNotification;
//    private Boolean isAllowCommentNotification;
//    private Boolean isAllowAdNotification;

}