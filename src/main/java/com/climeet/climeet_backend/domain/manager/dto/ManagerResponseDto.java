package com.climeet.climeet_backend.domain.manager.dto;

import com.climeet.climeet_backend.domain.manager.Manager;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ManagerResponseDto {

    @Getter
    public static class ManagerSimpleInfo {

        private String accessToken;
        private String refreshToken;

        public ManagerSimpleInfo(Manager manager) {
            this.accessToken = manager.getAccessToken();
            this.refreshToken = manager.getRefreshToken();

        }

    }

}
