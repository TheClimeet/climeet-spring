package com.climeet.climeet_backend.domain.routerecord;

import com.climeet.climeet_backend.domain.climbingrecord.ClimbingRecord;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.*;

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
    private ClimbingRecord climbingRecord;

    @OneToOne(fetch = FetchType.LAZY)
    private Route route;

    private int attemptCount;

    private Boolean isCompleted = false;

    public static RouteRecord toEntity(RouteRecordRequestDto requestDto,
        ClimbingRecord climbingRecord, Route route) {
        return RouteRecord.builder()
            .climbingRecord(climbingRecord)
            .route(route)
            .attemptCount(requestDto.getAttemptCount())
            .isCompleted(requestDto.getIsCompleted())
            .build();
    }
}