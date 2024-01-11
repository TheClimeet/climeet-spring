package com.climeet.climeet_backend.domain.climber.dto;

import com.climeet.climeet_backend.domain.climber.Climber;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import lombok.Data;

@Data
public class ClimberResponseDto {

    private SocialType socialType;
    private Long socialID;


    public ClimberResponseDto(Climber climber){
        this.socialType = climber.getSocialType();
        this.socialID = climber.getSocialId();
    }



}
