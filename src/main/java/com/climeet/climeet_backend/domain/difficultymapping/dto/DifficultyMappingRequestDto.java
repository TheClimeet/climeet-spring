package com.climeet.climeet_backend.domain.difficultymapping.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DifficultyMappingRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateDifficultyMappingRequest {

        private List<DifficultyMappingElement> elements;

    }

    @Getter
    @NoArgsConstructor
    public static class DifficultyMappingElement {

        private String climeetDifficultyName;
        private String gymDifficultyName;
        private String gymDifficultyColor;

    }


}
