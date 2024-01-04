package com.climeet.climeet_backend.domain.climbingrecord.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbingrecord.ClimbingRecord;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClimbingRecordResponseDto {
    @Getter
    @NoArgsConstructor
    public static class ClimbingRecordSimpleInfo{

        private LocalDate date;
        private String gymName;
        private String gymProfileImg;
        private LocalTime time;
        private Integer totalCompletedCount;
        private Integer totalAttemptCount;
        private Integer avgDifficulty;

        public ClimbingRecordSimpleInfo(ClimbingRecord climbingRecord) {
            this.date = climbingRecord.getClimbingDate();
            this.gymName = climbingRecord.getGym().getName();
            this.avgDifficulty = climbingRecord.getAvgDifficulty();
            this.time = climbingRecord.getClimbingTime();
            this.gymProfileImg = climbingRecord.getGym().getProfileImageUrl();
            this.totalAttemptCount = climbingRecord.getTotalAttemptCount();
            this.totalCompletedCount = climbingRecord.getTotalCompletedCount();
        }
    }
}
