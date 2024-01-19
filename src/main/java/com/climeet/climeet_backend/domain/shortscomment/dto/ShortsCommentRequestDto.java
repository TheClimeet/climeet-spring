package com.climeet.climeet_backend.domain.shortscomment.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShortsCommentRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateShortsCommentRequest {

        private String content;
    }
}
