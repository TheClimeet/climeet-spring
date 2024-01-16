package com.climeet.climeet_backend.domain.climber.dto;

import com.climeet.climeet_backend.domain.climber.Climber;
import com.climeet.climeet_backend.domain.climber.enums.ClimbingLevel;
import com.climeet.climeet_backend.domain.climber.enums.DiscoveryChannel;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import lombok.Data;

//do not use
@Data
public class ClimberLoginResponseDto {

    private SocialType socialType;
    private Long socialID;

    public ClimberLoginResponseDto(Climber climber){
       this.socialType = getSocialType();
       this.socialID = getSocialID();

    }


}

