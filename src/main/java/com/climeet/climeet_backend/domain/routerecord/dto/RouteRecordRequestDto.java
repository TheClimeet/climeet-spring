package com.climeet.climeet_backend.domain.routerecord.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class RouteRecordRequestDto {

    // TODO: 2024/01/04 sectorName, difficulty로 루트 구분하는 상황 고려
    // TODO: 2024/01/07 ClimbingRecord의 avgDifficulty 어떻게 구현 할 지 고민

    private Long routeId;

    //도전횟수
    private Integer attemptCount;

    //완등여부
    private Boolean isCompleted;
}
