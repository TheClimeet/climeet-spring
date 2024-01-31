package com.climeet.climeet_backend.domain.climbingrecord.dto;

import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto.CreateRouteRecord;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;


public class  ClimbingRecordRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateClimbingRecord {
        @Schema(example = "1", description = "짐 id")
        private Long gymId;

        @Schema(example = "LocalDate.of(2024, 1, 4)", description = "날짜입력")
        private LocalDate date;

        @Schema(example = "LocalTime.of(1, 30)", description = "시간")
        private LocalTime time;

        @Schema(example = "3", description = "평균 레벨")
        private int avgDifficulty;

        @Schema(description = "클라이밍 루트 목록")
        private List<CreateRouteRecord> routeRecordRequestDtoList;
    }

    @Getter
    @NoArgsConstructor
    public static class UpdateClimbingRecord {
        @Schema(example = "LocalDate.of(2024, 1, 4)", description = "날짜입력")
        private LocalDate date;

        @Schema(example = "LocalTime.of(1, 30)", description = "시간")
        private LocalTime time;

    }

}