package com.climeet.climeet_backend.domain.climbingrecord.dto;

import com.climeet.climeet_backend.domain.climbingrecord.ClimbingRecord;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordResponseDto.RouteRecordSimpleInfo;
import com.climeet.climeet_backend.domain.user.User;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

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
        private Map<String, Long> difficulty;

        public static ClimbingRecordStatisticsInfo toDTO(LocalTime time, Long totalCompletedCount,
            Long attemptRouteCount, Map<String, Long> difficulty) {

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
    public static class ClimbingRecordStatisticsInfoByGym {

        private LocalTime time;
        private Long totalCompletedCount;
        private Long attemptRouteCount;
        private List<GymDifficultyMappingInfo> difficulty;

        public static ClimbingRecordStatisticsInfoByGym toDTO(LocalTime time, Long totalCompletedCount,
            Long attemptRouteCount, List<GymDifficultyMappingInfo> difficulty) {

            return ClimbingRecordStatisticsInfoByGym.builder()
                .time(time)
                .totalCompletedCount(totalCompletedCount)
                .attemptRouteCount(attemptRouteCount)
                .difficulty(difficulty)
                .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @RequiredArgsConstructor
    @Builder
    public static class GymDifficultyMappingInfo{

        private String climeetDifficultyName;
        private String gymDifficultyName;
        private String gymDifficultyColor;
        private Long count;
        public static GymDifficultyMappingInfo toDTO(
            DifficultyMapping difficultyMapping,
            Long count
        ){
            return GymDifficultyMappingInfo.builder()
                .climeetDifficultyName(difficultyMapping.getClimeetDifficultyName())
                .gymDifficultyName(difficultyMapping.getGymDifficultyName())
                .gymDifficultyColor(difficultyMapping.getGymDifficultyColor())
                .count(count)
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
    public static class ClimbingRecordUserStatisticsSimpleInfo {

        private Long userId;
        private Long totalCompletedCount;
        private Long attemptRouteCount;
        private Map<String, Long> difficulty;

        public static ClimbingRecordUserStatisticsSimpleInfo toDTO(Long userId, Long totalCompletedCount,
            Long attemptRouteCount, Map<String, Long> difficulty) {

            return ClimbingRecordUserStatisticsSimpleInfo.builder()
                .userId(userId)
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
    public static class ClimbingRecordUserAndGymStatisticsDetailInfo {

        private Long userId;
        private Long totalCompletedCount;
        private Long attemptRouteCount;
        private List<GymDifficultyMappingInfo> difficulty;

        public static ClimbingRecordUserAndGymStatisticsDetailInfo toDTO(Long userId, Long totalCompletedCount,
            Long attemptRouteCount, List<GymDifficultyMappingInfo> difficulty) {

            return ClimbingRecordUserAndGymStatisticsDetailInfo.builder()
                .userId(userId)
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
    public static class BestClearUserSimpleInfo {

        private Long totalCompletedCount;
        private Long userId;
        protected String profileName;
        protected String profileImageUrl;
        private int ranking;

        public static BestClearUserSimpleInfo toDTO(User user, int ranking, Long totalCompletedCount) {
            return BestClearUserSimpleInfo.builder()
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
    public static class BestLevelUserSimpleInfo {

        private Long userId;
        protected String profileName;
        protected String profileImageUrl;
        private int ranking;
        private int highDifficulty;
        private int highDifficultyCount;
        private String climeetDifficultyName;
        private String gymDifficultyName;
        private String gymDifficultyColor;


        public static BestLevelUserSimpleInfo toDTO(User user, int ranking,
            int highDifficulty, int highDifficultyCount,
            String climeetDifficultyName, String gymDifficultyName, String gymDifficultyColor
            ) {
            return BestLevelUserSimpleInfo.builder()
                .ranking(ranking)
                .userId(user.getId())
                .profileImageUrl(user.getProfileImageUrl())
                .profileName(user.getProfileName())
                .highDifficulty(highDifficulty)
                .highDifficultyCount(highDifficultyCount)
                .climeetDifficultyName(climeetDifficultyName)
                .gymDifficultyName(gymDifficultyName)
                .gymDifficultyColor(gymDifficultyColor)
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BestTimeUserSimpleInfo {

        private LocalTime totalTime;

        private Long userId;
        protected String profileName;
        protected String profileImageUrl;
        private int ranking;

        public static BestTimeUserSimpleInfo toDTO(User user, int ranking, LocalTime totalTime) {

            return BestTimeUserSimpleInfo.builder()
                .ranking(ranking)
                .userId(user.getId())
                .profileImageUrl(user.getProfileImageUrl())
                .profileName(user.getProfileName())
                .totalTime(totalTime)
                .build();
        }
    }
}
