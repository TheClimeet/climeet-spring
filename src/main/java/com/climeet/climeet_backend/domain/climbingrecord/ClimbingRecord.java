package com.climeet.climeet_backend.domain.climbingrecord;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordRequestDto;
import com.climeet.climeet_backend.domain.routerecord.RouteRecord;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
public class ClimbingRecord extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClimbingGym gym;

    private LocalDate climbingDate;

    private LocalTime climbingTime;

    public static ClimbingRecord toEntity(ClimbingRecordRequestDto requestDto,
        ClimbingGym climbingGym) {
        return ClimbingRecord.builder()
            .gym(climbingGym)
            .build();
    }
}
