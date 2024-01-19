package com.climeet.climeet_backend.domain.climbingrecord.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbingrecord.ClimbingRecord;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

public class ClimbingRecordResponseDto {

    @Getter
    @NoArgsConstructor
    public static class ClimbingRecordSimpleInfo {

        private Long ClimbingRecordId;
        private LocalDate date;
        private LocalTime time;
        private Integer totalCompletedCount;
        private Integer totalAttemptCount;
        private Integer avgDifficulty;
        private Long gymId;

        public ClimbingRecordSimpleInfo(ClimbingRecord climbingRecord) {
            this.ClimbingRecordId = climbingRecord.getId();
            this.date = climbingRecord.getClimbingDate();
            this.avgDifficulty = climbingRecord.getAvgDifficulty();
            this.time = climbingRecord.getClimbingTime();
            this.totalAttemptCount = climbingRecord.getTotalAttemptCount();
            this.totalCompletedCount = climbingRecord.getTotalCompletedCount();
            this.gymId = climbingRecord.getId();
        }
    }

    @Getter
    @NoArgsConstructor
    @Setter
    @AllArgsConstructor
    public static class ClimbingRecordStatisticsInfo {

        private LocalTime time;
        private Integer totalCompletedCount;
        private Integer attemptRouteCount;
        private Integer avgDifficulty;

//        public ClimbingRecordStatisticsInfo(Long time, Integer totalCompletedCount, Integer attemptRouteCount, Integer avgDifficulty) {
//            this.time = time;
//            this.totalCompletedCount = totalCompletedCount;
//            this.attemptRouteCount = attemptRouteCount;
//            this.avgDifficulty = avgDifficulty;
//        }
    }
}
