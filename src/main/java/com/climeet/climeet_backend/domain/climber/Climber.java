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
import java.time.LocalDateTime;
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

    private boolean isShortsPublic = true;

    private boolean isHomeGymPublic = true;

    private boolean isAverageCompletionRatePublic = true;

    private boolean isAverageCompletionLevelPublic = true;

    public void updateIsShortsPublic(){
        this.isShortsPublic = !isShortsPublic;
    }

    public void updateIsHomeGymPublic(){
        this.isHomeGymPublic = !isHomeGymPublic;
    }

    public void updateIsAverageCompletionRatePublic(){
        this.isAverageCompletionRatePublic = !isAverageCompletionRatePublic;
    }

    public void updateIsAverageCompletionLevelPublic(){
        this.isAverageCompletionLevelPublic = !isAverageCompletionRatePublic;
    }


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

    public String getPayload(){
        return this.getId()+"+climber";
    }

    public static Climber toEntity(String socialId, SocialType socialType, String profileImg){
        return Climber.builder()
            .socialId(socialId)
            .socialType(socialType)
            .profileImageUrl(profileImg)
            .status(true)
            .isShortsPublic(true)
            .isHomeGymPublic(true)
            .isAverageCompletionLevelPublic(true)
            .isAverageCompletionRatePublic(true)
            .followerCount(0L)
            .followingCount(0L)
            .thisWeekCompleteCount(0)
            .thisWeekTotalClimbingTime(0L)
            .createdAt(LocalDateTime.now())
            .build();
    }
}