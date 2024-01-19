package com.climeet.climeet_backend.domain.manager;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymService;
import com.climeet.climeet_backend.domain.manager.dto.ManagerRequestDto.CreateManagerRequest;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import java.util.Optional;
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
public class Manager extends User {

    @OneToOne
    @JoinColumn(name="climbing_gym_id")
    private ClimbingGym climbingGym;

    @NotNull
    private String loginId;

    @NotNull
    private String password;

    @NotNull
    private String name;

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
            .name(createManagerRequest.getName())
            .phoneNumber(createManagerRequest.getPhoneNumber())
            .email(createManagerRequest.getEmail())
            .isRegistered(true)
            .climbingGym(gym)
            .build();
    }

    public void updateClimbingGym(ClimbingGym gym){
        this.climbingGym = gym;
    }

}