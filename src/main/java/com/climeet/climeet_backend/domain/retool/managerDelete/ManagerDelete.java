package com.climeet.climeet_backend.domain.retool.managerDelete;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
public class ManagerDelete extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Manager manager;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClimbingGym climbingGym;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus approvalStatus;

    private LocalDateTime applyAt;

    public static ManagerDelete toEntity(Manager manager){
        return ManagerDelete.builder()
            .manager(manager)
            .approvalStatus(ApprovalStatus.PENDING)
            .climbingGym(manager.getClimbingGym())
            .applyAt(LocalDateTime.now())
            .build();
    }

    public void updateApprovalStatus(Boolean isApproved){
        if(isApproved) {
            this.approvalStatus = ApprovalStatus.APPROVED;
        }
        else {
            this.approvalStatus = ApprovalStatus.REJECTED;
        }
    }

    public void deleteManager(){
        this.manager = null;
    }


}
