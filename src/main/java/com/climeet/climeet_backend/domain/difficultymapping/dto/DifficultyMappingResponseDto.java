package com.climeet.climeet_backend.domain.difficultymapping.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteDetailResponse;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionResponseDto;
import com.climeet.climeet_backend.domain.sector.dto.SectorResponseDto.SectorDetailResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DifficultyMappingResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DifficultyMappingDetailResponse {

        private String climeetDifficultyName;
        private String gymDifficultyName;
        private int gymDifficulty;
        private String gymDifficultyColor;

        public static DifficultyMappingDetailResponse toDto(
            DifficultyMapping difficultyMapping) {
            return DifficultyMappingDetailResponse.builder()
                .climeetDifficultyName(difficultyMapping.getClimeetDifficulty().getStringValue())
                .gymDifficultyName(difficultyMapping.getGymDifficultyName())
                .gymDifficulty(difficultyMapping.getGymDifficulty())
                .gymDifficultyColor(difficultyMapping.getGymDifficultyColor())
                .build();
        }
    }

}
