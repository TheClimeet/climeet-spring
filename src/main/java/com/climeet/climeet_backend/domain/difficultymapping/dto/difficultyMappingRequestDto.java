package com.climeet.climeet_backend.domain.difficultymapping.dto;

import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class difficultyMappingRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateDifficultyMappingRequest {

        private List<DifficultyMappingElement> elements;

    }

    @Getter
    @NoArgsConstructor
    public static class DifficultyMappingElement {

        private String climeetDifficulty;
        private String gymDifficultyName;
        private String gymDifficultyColor;

    }


}
