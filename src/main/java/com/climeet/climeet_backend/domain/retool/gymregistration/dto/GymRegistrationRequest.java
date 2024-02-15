package com.climeet.climeet_backend.domain.retool.gymregistration.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class GymRegistrationRequest {

    @Getter
    @NoArgsConstructor
    public static class PatchGymRegistrationReq {

        private Boolean isApproved;
        private String content;
    }
}
