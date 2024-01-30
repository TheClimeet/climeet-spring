package com.climeet.climeet_backend.domain.difficultymapping.dto;

import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
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
