package com.climeet.climeet_backend.domain.difficultymapping.dto;

import java.util.Map;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DifficultyMappingRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateDifficultyMappingRequest {

        private Map<String, String> gymClimeetDifficulty;
    }

}
