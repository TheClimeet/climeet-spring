package com.climeet.climeet_backend.domain.difficultymapping.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class difficultyMappingRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateDifficultyMappingRequest {

        private String climeetDifficulty;
        private int gymDifficulty;
        private String gymDifficultyName;
        private String gymDifficultyColor;

    }

}
