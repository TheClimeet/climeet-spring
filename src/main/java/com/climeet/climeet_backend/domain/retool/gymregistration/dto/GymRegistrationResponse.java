package com.climeet.climeet_backend.domain.retool.gymregistration.dto;

import static com.climeet.climeet_backend.domain.climbinggym.BitmaskConverter.convertBitmaskToServiceList;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.enums.ServiceBitmask;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.retool.gymregistration.ApprovalStatus;
import com.climeet.climeet_backend.domain.retool.gymregistration.GymRegistration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GymRegistrationResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetGymRegistrationsSimpleInfo {

        private Long id;
        private String gymName;
        private LocalDateTime createdAt;
        private String managerId;
        private ApprovalStatus approvalStatus;
        private LocalDate approvedAt;

        public static GetGymRegistrationsSimpleInfo toDTO(GymRegistration gymRegistration,
            ClimbingGym climbingGym, Manager manager) {
            return GetGymRegistrationsSimpleInfo.builder().id(gymRegistration.getId())
                .gymName(climbingGym.getName()).createdAt(gymRegistration.getCreatedAt())
                .managerId(manager.getLoginId()).approvalStatus(gymRegistration.getApprovalStatus())
                .approvedAt(gymRegistration.getApprovedAt()).build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetGymRegistrationsDetailInfo {

        private LocalDateTime createdAt;
        private String gymName;
        private String profileImageUrl;
        private String backgroundImageUrl;
        private String businessRegistrationImageUrl;
        private List<ServiceBitmask> serviceBitmaskList;
        private String managerLoginId;
        private String managerName;
        private String managerPhoneNumber;
        private String managerEmail;

        public static GetGymRegistrationsDetailInfo toDTO(GymRegistration gymRegistration,
            ClimbingGym climbingGym, Manager manager) {
            return GetGymRegistrationsDetailInfo.builder()
                .createdAt(gymRegistration.getCreatedAt())
                .gymName(climbingGym.getName())
                .profileImageUrl(climbingGym.getProfileImageUrl())
                .backgroundImageUrl(climbingGym.getBackgroundImageList().get(0).getImgUrl())
                .businessRegistrationImageUrl(manager.getBusinessRegistrationImageUrl())
                .serviceBitmaskList(convertBitmaskToServiceList(climbingGym.getServiceBitMask()))
                .managerLoginId(manager.getLoginId())
                .managerName(manager.getProfileName())
                .managerPhoneNumber(manager.getPhoneNumber())
                .managerEmail(manager.getEmail())
                .build();
        }
    }
}