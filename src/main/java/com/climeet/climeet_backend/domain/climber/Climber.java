package com.climeet.climeet_backend.domain.climber;

import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.climber.enums.ClimbingLevel;
import com.climeet.climeet_backend.domain.climber.enums.DiscoveryChannel;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import jakarta.persistence.Column;
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
    @Column(name = "social_id")
    private String socialId;

    @NotNull
    @Column(name = "access_token")
    private String accessToken;

    @NotNull
    @Column(name = "refresh_token")
    private String refreshToken;

    @NotNull
    @Column(name = "nick_name")
    private String nickName;

    @Column(name = "profile_image_url")
    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(name = "climbing_level")
    private ClimbingLevel climbingLevel;

    @Enumerated(EnumType.STRING)
    @Column(name = "social_type")
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    @Column(name = "discovery_channel")
    private DiscoveryChannel discoveryChannel;

    private boolean status = true;


    @Builder
    public Climber(String socialId, String accessToken, String refreshToken, String nickName, String profileImageUrl, ClimbingLevel climbingLevel, SocialType socialType, DiscoveryChannel discoveryChannel){
        this.socialId = socialId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
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

    public void updateToken(String accessToken, String refreshToken) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }

    public void updateClimbingLevel(ClimbingLevel climbingLevel) {
        this.climbingLevel = climbingLevel;
    }

    public void updateDiscoveryChannel(DiscoveryChannel discoveryChannel) {
        this.discoveryChannel = discoveryChannel;
    }

    public void updateSocialType(SocialType socialType) {
        this.socialType = socialType;
    }

    public void updateProfileImageUrl(String profileImgUrl) {
        this.profileImageUrl = profileImgUrl;
    }










}