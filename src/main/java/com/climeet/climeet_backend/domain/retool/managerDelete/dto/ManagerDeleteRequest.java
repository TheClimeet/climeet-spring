package com.climeet.climeet_backend.domain.retool.managerDelete.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class ManagerDeleteRequest {

    @Getter
    @NoArgsConstructor
    public static class PatchManagerDeleteReq{
        private Boolean isApproved;

    }

}
