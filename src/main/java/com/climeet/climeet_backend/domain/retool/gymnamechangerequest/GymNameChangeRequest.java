package com.climeet.climeet_backend.domain.retool.gymnamechangerequest;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
public class GymNameChangeRequest extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ClimbingGym climbingGym;

    private String afterGymName;

    public static GymNameChangeRequest toEntity(ClimbingGym climbingGym, String afterGymName){
        return GymNameChangeRequest.builder()
            .climbingGym(climbingGym)
            .afterGymName(afterGymName)
            .build();
    }
}