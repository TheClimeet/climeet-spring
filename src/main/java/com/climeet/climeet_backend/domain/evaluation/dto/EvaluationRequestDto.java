package com.climeet.climeet_backend.domain.evaluation.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class EvaluationRequestDto {
    @Getter
    @NoArgsConstructor
    public static class CreateEvaluation {
        private String content;
        private Float rating;
    }
}
