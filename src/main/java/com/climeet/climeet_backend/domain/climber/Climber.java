package com.climeet.climeet_backend.domain.climber;

import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.climber.enums.ClimbingLevel;
import com.climeet.climeet_backend.domain.climber.enums.DiscoveryChannel;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
@Setter
public class Climber extends User {

    @NotNull
    private Long socialId;

    @NotNull
    private String accessToken;

    @NotNull
    private String refreshToken;

    @NotNull
    private String nickName;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private ClimbingLevel climbingLevel;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private DiscoveryChannel discoveryChannel;

    private boolean status = true;


    @Builder
    public Climber(Long socialId, String accessToken, String nickName, String profileImageUrl, ClimbingLevel climbingLevel, SocialType socialType, DiscoveryChannel discoveryChannel){
        this.socialId = socialId;
        this.accessToken = accessToken;
        this.nickName = nickName;
        this.profileImageUrl = profileImageUrl;
        this.climbingLevel = climbingLevel;
        this.socialType = socialType;
        this.discoveryChannel = discoveryChannel;
    }

    public void update(String nickname, ClimbingLevel climbingLevel, DiscoveryChannel discoveryChannel){
        this.nickName = nickname;
        this.climbingLevel = climbingLevel;
        this.discoveryChannel = discoveryChannel;
    }
    public void updateNickName(String nickName){
        this.nickName = nickName;
    }
    public void setToken(String accessToken, String refreshToken){
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }




}