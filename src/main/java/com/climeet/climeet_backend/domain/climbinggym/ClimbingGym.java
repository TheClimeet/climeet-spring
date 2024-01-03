package com.climeet.climeet_backend.domain.climbinggym;

import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ClimbingGym extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String name;

    private String profileImageUrl;

    private Float AverageRating = 0.0F;

    private String layoutImageUrl;

    private int reviewCount = 0;

    private int selectionCount = 0;

//    @Enumerated(EnumType.STRING)
//    private ClimbingGymService climbingGymService;
}