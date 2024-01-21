package com.climeet.climeet_backend.domain.manager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class ManagerResponseDto {

    @Getter
    @NoArgsConstructor
    public static class ManagerSimpleInfo{
        private String accessToken;
        private String refreshToken;

    }


}
