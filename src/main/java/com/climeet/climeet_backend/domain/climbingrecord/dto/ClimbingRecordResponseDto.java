package com.climeet.climeet_backend.domain.climbingrecord.dto;

import com.climeet.climeet_backend.domain.climbingrecord.ClimbingRecord;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordResponseDto.RouteRecordSimpleInfo;
import com.climeet.climeet_backend.domain.user.User;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClimbingRecordResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClimbingRecordSimpleInfo {

        private Long climbingRecordId;
        private LocalDate date;
        private LocalTime time;
        private Integer totalCompletedCount;
        private Integer totalAttemptCount;
        private Integer avgDifficulty;
        private Long gymId;

        public static ClimbingRecordSimpleInfo toDTO(ClimbingRecord climbingRecord) {
            return ClimbingRecordSimpleInfo.builder()
                .climbingRecordId(climbingRecord.getId())
                .date(climbingRecord.getClimbingDate())
                .avgDifficulty(climbingRecord.getAvgDifficulty())
                .time(climbingRecord.getClimbingTime())
                .totalAttemptCount(climbingRecord.getTotalAttemptCount())
                .totalCompletedCount(climbingRecord.getTotalCompletedCount())
                .gymId(climbingRecord.getGym().getId())
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClimbingRecordDetailInfo {

        private Long climbingRecordId;
        private LocalDate date;
        private LocalTime time;
        private Integer totalCompletedCount;
        private Integer totalAttemptCount;
        private Integer avgDifficulty;
        private Long gymId;
        private List<RouteRecordSimpleInfo> routeRecordSimpleInfoList;

        public static ClimbingRecordDetailInfo toDTO(ClimbingRecord climbingRecord,
            List<RouteRecordSimpleInfo> routeRecordSimpleInfoList) {

            return ClimbingRecordDetailInfo.builder()
                .climbingRecordId(climbingRecord.getId())
                .date(climbingRecord.getClimbingDate())
                .avgDifficulty(climbingRecord.getAvgDifficulty())
                .time(climbingRecord.getClimbingTime())
                .totalAttemptCount(climbingRecord.getTotalAttemptCount())
                .totalCompletedCount(climbingRecord.getTotalCompletedCount())
                .gymId(climbingRecord.getGym().getId())
                .routeRecordSimpleInfoList(routeRecordSimpleInfoList)
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClimbingRecordStatisticsInfo {

        private LocalTime time;
        private Long totalCompletedCount;
        private Long attemptRouteCount;
        private List<Map<Long, Long>> difficulty;

        public static ClimbingRecordStatisticsInfo toDTO(LocalTime time, Long totalCompletedCount,
            Long attemptRouteCount, List<Map<Long, Long>> difficulty) {

            return ClimbingRecordStatisticsInfo.builder()
                .time(time)
                .totalCompletedCount(totalCompletedCount)
                .attemptRouteCount(attemptRouteCount)
                .difficulty(difficulty)
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ClimbingRecordStatisticsSimpleInfo {

        private List<Map<Long, Long>> difficulty;

        public static ClimbingRecordStatisticsSimpleInfo toDTO(List<Map<Long, Long>> difficulty) {

            return ClimbingRecordStatisticsSimpleInfo.builder()
                .difficulty(difficulty)
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BestClearUserSimple {

        private Long totalCompletedCount;
        private Long userId;
        protected String profileName;
        protected String profileImageUrl;
        private int ranking;

        public static BestClearUserSimple toDTO(User user, int ranking, Long totalCompletedCount) {
            return BestClearUserSimple.builder()
                .ranking(ranking)
                .userId(user.getId())
                .profileImageUrl(user.getProfileImageUrl())
                .profileName(user.getProfileName())
                .totalCompletedCount(totalCompletedCount)
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BestLevelUserSimple {

        private int highDifficulty;
        private Long userId;
        protected String profileName;
        protected String profileImageUrl;
        private int ranking;

        public static BestLevelUserSimple toDTO(User user, int ranking, int highDifficulty) {
            return BestLevelUserSimple.builder()
                .ranking(ranking)
                .userId(user.getId())
                .profileImageUrl(user.getProfileImageUrl())
                .profileName(user.getProfileName())
                .highDifficulty(highDifficulty)
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BestTimeUserSimple {

        private LocalTime totalTime;

        private Long userId;
        protected String profileName;
        protected String profileImageUrl;
        private int ranking;
        public static BestTimeUserSimple toDTO(User user, int ranking, LocalTime totalTime) {

            return BestTimeUserSimple.builder()
                .ranking(ranking)
                .userId(user.getId())
                .profileImageUrl(user.getProfileImageUrl())
                .profileName(user.getProfileName())
                .totalTime(totalTime)
                .build();
        }
    }
}
