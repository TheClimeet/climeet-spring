package com.climeet.climeet_backend.domain.climbingrecord.dto;

import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
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

    @Schema(example = "1", description = "짐 id")
    private Long gymId;

    @Schema(example = "LocalDate.of(2024, 1, 4)", description = "날짜입력")
    private LocalDate date;

    @Schema(example = "LocalTime.of(1, 30)", description = "시간")
    private LocalTime time;

    @Schema(description = "클라이밍 루트 목록")
    private List<RouteRecordRequestDto> routeRecordRequestDtoList;

}