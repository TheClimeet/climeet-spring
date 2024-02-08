package com.climeet.climeet_backend.domain.manager;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.manager.dto.ManagerRequestDto.CreateManagerRequest;
import com.climeet.climeet_backend.domain.user.User;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@SuperBuilder
public class Manager extends User {

    @OneToOne
    @JoinColumn(name="climbing_gym_id")
    private ClimbingGym climbingGym;

    @NotNull
    private String loginId;

    @NotNull
    private String password;

    @NotNull
    private String phoneNumber;

    @NotNull
    private String email;

    private String businessRegistrationImageUrl;

    private Boolean isRegistered = false;

    public void setClimbingGym(ClimbingGym climbingGym){
        this.climbingGym = climbingGym;
    }

    public static Manager toEntity(CreateManagerRequest createManagerRequest, ClimbingGym gym){

        return Manager.builder()
            .loginId(createManagerRequest.getLoginId())
            .password(createManagerRequest.getPassword())
            .profileName(createManagerRequest.getName())
            .phoneNumber(createManagerRequest.getPhoneNumber())
            .email(createManagerRequest.getEmail())
            .isRegistered(false)
            .climbingGym(gym)
            .followerCount(0L)
            .followingCount(0L)
            .thisWeekCompleteCount(0)
            .thisWeekTotalClimbingTime(0L)
            .createdAt(LocalDateTime.now())
            .build();
    }

    public void updateClimbingGym(ClimbingGym gym){
        this.climbingGym = gym;
    }

    public Manager hashPassword(PasswordEncoder passwordEncoder){
        this.password = passwordEncoder.encode(this.password);
        return this;
    }

    public boolean checkPassword(String plainPassword, PasswordEncoder passwordEncoder){
        return passwordEncoder.matches(plainPassword, this.password);
    }

    public String getPayload(){
        return this.getId()+"+manager";
    }
}