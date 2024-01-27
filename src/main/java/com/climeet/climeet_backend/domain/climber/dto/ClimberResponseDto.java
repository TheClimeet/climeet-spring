package com.climeet.climeet_backend.domain.climber.dto;

import com.climeet.climeet_backend.domain.climber.Climber;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

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
    @Getter
    public static class ClimberTokenRefreshResponse{
        @JsonProperty("access_token")
        private String accessToken;
    }




}
