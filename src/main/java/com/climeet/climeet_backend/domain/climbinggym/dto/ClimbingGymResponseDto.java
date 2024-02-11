package com.climeet.climeet_backend.domain.climbinggym.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
import com.climeet.climeet_backend.domain.manager.Manager;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ClimbingGymResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClimbingGymSimpleResponse {

        private Long id;
        private String name;
        private Long managerId;

        public static ClimbingGymSimpleResponse toDTO(ClimbingGym climbingGym) {
            Long managerId = null;
            if (climbingGym.getManager() != null) {
                managerId = climbingGym.getManager().getId();
            }
            return ClimbingGymSimpleResponse.builder()
                .id(climbingGym.getId())
                .name(climbingGym.getName())
                .managerId(managerId)
                .build();
        }

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcceptedClimbingGymSimpleResponse {

        private Long id;
        private String name;
        private Long managerId;
        private Long follower;
        private String profileImageUrl;

        public static AcceptedClimbingGymSimpleResponse toDTO(ClimbingGym climbingGym,
            Long managerId, Long follower, String profileImageUrl) {

            return AcceptedClimbingGymSimpleResponse.builder()
                .id(climbingGym.getId())
                .name(climbingGym.getName())
                .managerId(managerId)
                .follower(follower)
                .profileImageUrl(profileImageUrl)
                .build();
        }

    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClimbingGymDetailResponse {

        private String gymProfileImageUrl;
        private String gymBackGroundImageUrl;
        private String gymName;
        private Long followerCount;
        private Long followingCount;
        private float averageRating;
        private int reviewCount;

        public static ClimbingGymDetailResponse toDto(ClimbingGym climbingGym, Manager manager,
            String gymBackGroundImageUrl) {
            return ClimbingGymDetailResponse.builder()
                .gymProfileImageUrl(climbingGym.getProfileImageUrl())
                .gymBackGroundImageUrl(gymBackGroundImageUrl)
                .gymName(climbingGym.getName())
                .followerCount(manager.getFollowerCount())
                .followingCount(manager.getFollowingCount())
                .averageRating(climbingGym.getAverageRating())
                .reviewCount(climbingGym.getReviewCount())
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClimbingGymTabInfoResponse {

        private Long gymId;
        private String address;
        private String tel;
        private Map<String, List<String>> businessHours;
        private List<String> serviceList;

        public static ClimbingGymTabInfoResponse toDto(ClimbingGym climbingGym,
            Map<String, List<String>> businessHours, List<String> serviceList) {
            return ClimbingGymTabInfoResponse.builder()
                .gymId(climbingGym.getId())
                .address(climbingGym.getAddress())
                .tel(climbingGym.getTel())
                .businessHours(businessHours)
                .serviceList(serviceList)
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClimbingGymInfoResponse {

        private String name;
        private String address;
        private String tel;
        private Map<String, List<String>> businessHours;

        public static ClimbingGymInfoResponse toDto(ClimbingGym climbingGym,
            Map<String, List<String>> businessHours) {
            return ClimbingGymInfoResponse.builder()
                .name(climbingGym.getName())
                .address(climbingGym.getAddress())
                .tel(climbingGym.getTel())
                .businessHours(businessHours)
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClimbingGymAverageLevelDetailResponse {

        private int difficulty;
        private String gymDifficultyName;
        private String gymDifficultyColor;
        private double percentage;

        public static ClimbingGymAverageLevelDetailResponse toDto(
            DifficultyMapping difficultyMapping, double percentage) {
            return ClimbingGymAverageLevelDetailResponse.builder()
                .difficulty(difficultyMapping.getDifficulty())
                .gymDifficultyName(difficultyMapping.getGymDifficultyName())
                .gymDifficultyColor(difficultyMapping.getGymDifficultyColor())
                .percentage(percentage)
                .build();
        }
    }

}
