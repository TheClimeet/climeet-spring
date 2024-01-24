package com.climeet.climeet_backend.domain.routerecord;

import com.climeet.climeet_backend.domain.climbingrecord.ClimbingRecord;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto.CreateRouteRecordDto;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
public class RouteRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private ClimbingRecord climbingRecord;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private Route route;

    private LocalDate routeRecordDate;

    private int attemptCount;

    private Boolean isCompleted = false;

    public static RouteRecord toEntity(User user, CreateRouteRecordDto createRouteRecordReq,
        ClimbingRecord climbingRecord, Route route) {
        return RouteRecord.builder()
            .user(user)
            .climbingRecord(climbingRecord)
            .route(route)
            .attemptCount(createRouteRecordReq.getAttemptCount())
            .isCompleted(createRouteRecordReq.getIsCompleted())
            .routeRecordDate(climbingRecord.getClimbingDate())
            .build();
    }

    public void update(int attemptCount, Boolean isCompleted, Route route) {
        this.route = route;
        this.attemptCount = attemptCount;
        this.isCompleted = isCompleted;
    }

}