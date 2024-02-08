package com.climeet.climeet_backend.domain.routerecord;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbingrecord.ClimbingRecord;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto.CreateRouteRecord;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private ClimbingGym gym;

    private LocalDate routeRecordDate;

    private int attemptCount;

    private Boolean isCompleted = false;

    private int difficulty;

    public static RouteRecord toEntity(User user, CreateRouteRecord createRouteRecordReq,
        ClimbingRecord climbingRecord, Route route) {
        return RouteRecord.builder()
            .user(user)
            .climbingRecord(climbingRecord)
            .route(route)
            .gym(climbingRecord.getGym())
            .attemptCount(createRouteRecordReq.getAttemptCount())
            .isCompleted(createRouteRecordReq.getIsCompleted())
            .routeRecordDate(climbingRecord.getClimbingDate())
            .difficulty(route.getDifficultyMapping().getDifficulty())
            .build();
    }

    public void update(int attemptCount, Boolean isCompleted) {
        this.attemptCount = attemptCount;
        this.isCompleted = isCompleted;
    }

}