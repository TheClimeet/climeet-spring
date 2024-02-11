package com.climeet.climeet_backend.domain.retool.gymregistration;

public enum ApprovalStatus {
    APPROVED("승인"),
    REJECTED("미승인"),
    PENDING("승인 전");

    private final String description;

    ApprovalStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}