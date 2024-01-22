package com.climeet.climeet_backend.domain.climber.dto;

import com.climeet.climeet_backend.domain.climber.Climber;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import lombok.Data;

@Data
public class ClimberResponseDto {

    private SocialType socialType;
    private String accessToken;
    private String refreshToken;


    public ClimberResponseDto(Climber climber){
        this.socialType = climber.getSocialType();
        this.accessToken = climber.getAccessToken();
        this.refreshToken = climber.getRefreshToken();
    }




}
