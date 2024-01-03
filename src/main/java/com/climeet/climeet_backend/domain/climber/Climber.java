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
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Climber extends User {

    @NotNull
    private String token;

    @NotNull
    private String nickname;

    private String profileImageUrl;

    @Enumerated(EnumType.STRING)
    private ClimbingLevel climbingLevel;

    @Enumerated(EnumType.STRING)
    private SocialType socialType;

    @Enumerated(EnumType.STRING)
    private DiscoveryChannel discoveryChannel;

    private boolean status = true;
}