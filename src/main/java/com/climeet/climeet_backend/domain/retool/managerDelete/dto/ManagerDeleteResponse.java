package com.climeet.climeet_backend.domain.retool.managerDelete.dto;

import com.climeet.climeet_backend.domain.retool.managerDelete.ApprovalStatus;
import com.climeet.climeet_backend.domain.retool.managerDelete.ManagerDelete;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


public class ManagerDeleteResponse {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetManagerDeleteDetail{
        private Long id;
        private Long managerId;
        private String managerName;
        private Long gymId;
        private String gymName;
        private LocalDateTime applyAt;
        private ApprovalStatus approvalStatus;

        public static GetManagerDeleteDetail toDTO(ManagerDelete managerDelete){
            return GetManagerDeleteDetail.builder()
                .id(managerDelete.getId())
                .managerId(managerDelete.getManager().getId())
                .managerName(managerDelete.getManager().getProfileName())
                .gymId(managerDelete.getClimbingGym().getId())
                .gymName(managerDelete.getClimbingGym().getName())
                .applyAt(managerDelete.getApplyAt())
                .approvalStatus(managerDelete.getApprovalStatus())
                .build();
        }


    }

}
