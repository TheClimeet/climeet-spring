package com.climeet.climeet_backend.domain.climbingrecord.dto;

import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class ClimbingRecordRequestDto {

    @Schema(example = "2024-01-04", description = "날짜입력")
    private String date;

    @Schema(example = "120", description = "분단위 시간")
    private int time;

    @Schema(example = "더클라임 홍대점", description = "클라이밍 지점 이름")
    private String climbingGymName;

    @Schema(description = "클라이밍 루트 목록")
    private List<RouteRecordRequestDto> routeRecordRequestDtoList;
}