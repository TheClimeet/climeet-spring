package com.climeet.climeet_backend.domain.climbingrecord.dto;

import com.climeet.climeet_backend.domain.climbingrecord.ClimbingRecord;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordResponseDto.RouteRecordSimpleInfo;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
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
    public static class ClimbingRecordDetailInfo {

        private Long ClimbingRecordId;
        private LocalDate date;
        private LocalTime time;
        private Integer totalCompletedCount;
        private Integer totalAttemptCount;
        private Integer avgDifficulty;
        private Long gymId;
        private List<RouteRecordSimpleInfo> routeRecordSimpleInfoList;

        public ClimbingRecordDetailInfo(ClimbingRecord climbingRecord,
            List<RouteRecordSimpleInfo> routeRecordSimpleInfoList) {
            this.ClimbingRecordId = climbingRecord.getId();
            this.date = climbingRecord.getClimbingDate();
            this.avgDifficulty = climbingRecord.getAvgDifficulty();
            this.time = climbingRecord.getClimbingTime();
            this.totalAttemptCount = climbingRecord.getTotalAttemptCount();
            this.totalCompletedCount = climbingRecord.getTotalCompletedCount();
            this.gymId = climbingRecord.getGym().getId();
            this.routeRecordSimpleInfoList = routeRecordSimpleInfoList;
        }
    }

    @Getter
    @NoArgsConstructor
    @Setter
    @AllArgsConstructor
    public static class ClimbingRecordStatisticsInfo {

        private LocalTime time;
        private Long totalCompletedCount;
        private Long attemptRouteCount;
        private List<Map<Long, Long>> difficulty;

    }
}
