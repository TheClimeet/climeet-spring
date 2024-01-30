package com.climeet.climeet_backend.domain.difficultymapping;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingRequestDto.DifficultyMappingElement;
import com.climeet.climeet_backend.domain.difficultymapping.enums.ClimeetDifficulty;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
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
public class DifficultyMapping extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClimbingGym climbingGym;

    @NotNull
    private ClimeetDifficulty climeetDifficulty;

    @NotNull
    private int gymDifficulty;

    @NotNull
    @Column(length = 10)
    private String gymDifficultyName;

    @NotNull
    @Column(length = 7)
    private String gymDifficultyColor;

    public static DifficultyMapping toEntity(DifficultyMappingElement requestDto,
        ClimbingGym climbingGym, ClimeetDifficulty climeetDifficulty, int gymDifficulty) {
        return DifficultyMapping.builder()
            .climbingGym(climbingGym)
            .climeetDifficulty(climeetDifficulty)
            .gymDifficulty(gymDifficulty)
            .gymDifficultyName(requestDto.getGymDifficultyName())
            .gymDifficultyColor(requestDto.getGymDifficultyColor())
            .build();
    }
}