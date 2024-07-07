package com.climeet.climeet_backend.domain.retool.managerDelete;

import lombok.Getter;

@Getter
public enum ApprovalStatus {
    APPROVED("승인"),
    REJECTED("미승인"),
    PENDING("탈퇴 대기중");

    private final String description;

    ApprovalStatus(String description) {
        this.description = description;
    }

}