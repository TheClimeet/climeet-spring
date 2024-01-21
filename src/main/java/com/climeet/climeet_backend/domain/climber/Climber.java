package com.climeet.climeet_backend.domain.climber;

import com.climeet.climeet_backend.domain.climber.enums.ClimbingLevel;
import com.climeet.climeet_backend.domain.climber.enums.DiscoveryChannel;
import com.climeet.climeet_backend.domain.climber.enums.SocialType;
import com.climeet.climeet_backend.domain.user.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SuperBuilder
public class Climber extends User {

    @NotNull
    @Column(name = "social_id")
    private String socialId;

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


    public void updateProfileName(String profileName) {
        this.profileName = profileName; // User 클래스의 profileName 필드를 업데이트
    }

    public void updateClimbingLevel(ClimbingLevel climbingLevel) {
        this.climbingLevel = climbingLevel;
    }

    public void updateDiscoveryChannel(DiscoveryChannel discoveryChannel) {
        this.discoveryChannel = discoveryChannel;
    }

    public void updateProfileImageUrl(String profileImgUrl) {
        this.profileImageUrl = profileImgUrl;
    }
}