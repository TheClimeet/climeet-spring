package com.climeet.climeet_backend.domain.retool.gymduplicationrequest.dto;

import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.retool.gymduplicationrequest.GymDuplicationRequest;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GymDuplicationRequestResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GymDuplicationRequestSimpleInfo {

        private Long id;
        private LocalDateTime createdAt;
        private String managerPhoneNumber;
        private String managerEmail;
        private String gymRelationship;

        public static GymDuplicationRequestSimpleInfo toDTO(
            GymDuplicationRequest gymDuplicationRequest, Manager manager) {
            return GymDuplicationRequestSimpleInfo.builder()
                .id(gymDuplicationRequest.getId())
                .createdAt(gymDuplicationRequest.getCreatedAt())
                .managerPhoneNumber(manager.getPhoneNumber())
                .managerEmail(manager.getEmail())
                .gymRelationship(manager.getClass().getSimpleName())
                .build();
        }
    }
}