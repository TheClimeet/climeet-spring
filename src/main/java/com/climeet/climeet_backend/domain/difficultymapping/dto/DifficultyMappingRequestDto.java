package com.climeet.climeet_backend.domain.difficultymapping.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DifficultyMappingRequestDto {

    @Getter
    @AllArgsConstructor
    public static class CreateDifficultyMappingRequest {

        private String gymDifficultyName;
        private String climeetDifficultyName;
    }

}
