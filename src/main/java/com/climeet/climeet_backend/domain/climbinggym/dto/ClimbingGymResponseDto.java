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

        private Long gymId;
        private String gymProfileImageUrl;
        private String gymBackGroundImageUrl;
        private String gymName;
        private Long followerCount;
        private Long followingCount;
        private float averageRating;
        private int reviewCount;
        private Boolean isFollower;

        public static ClimbingGymDetailResponse toDTO(ClimbingGym climbingGym, Manager manager,
            String gymBackGroundImageUrl, Boolean isFollower) {
            return ClimbingGymDetailResponse.builder()
                .gymId(climbingGym.getId())
                .gymProfileImageUrl(climbingGym.getProfileImageUrl())
                .gymBackGroundImageUrl(gymBackGroundImageUrl)
                .gymName(climbingGym.getName())
                .followerCount(manager.getFollowerCount())
                .followingCount(manager.getFollowingCount())
                .averageRating(climbingGym.getAverageRating())
                .reviewCount(climbingGym.getReviewCount())
                .isFollower(isFollower)
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
        private String location;
        private String tel;
        private Map<String, List<String>> businessHours;
        private List<String> serviceList;
        private Map<String, String> priceList;

        public static ClimbingGymTabInfoResponse toDTO(ClimbingGym climbingGym,
            Map<String, List<String>> businessHours, List<String> serviceList,
            Map<String, String> priceList) {
            return ClimbingGymTabInfoResponse.builder()
                .gymId(climbingGym.getId())
                .address(climbingGym.getAddress())
                .location(climbingGym.getLocation())
                .tel(climbingGym.getTel())
                .businessHours(businessHours)
                .serviceList(serviceList)
                .priceList(priceList)
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClimbingGymInfoResponse {

        private Long gymId;
        private String name;
        private String address;
        private String tel;
        private Map<String, List<String>> businessHours;

        public static ClimbingGymInfoResponse toDTO(ClimbingGym climbingGym,
            Map<String, List<String>> businessHours) {
            return ClimbingGymInfoResponse.builder()
                .gymId(climbingGym.getId())
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

        private Long gymId;
        private int difficulty;
        private String gymDifficultyName;
        private String gymDifficultyColor;
        private double percentage;

        public static ClimbingGymAverageLevelDetailResponse toDTO(
            DifficultyMapping difficultyMapping, double percentage) {
            return ClimbingGymAverageLevelDetailResponse.builder()
                .gymId(difficultyMapping.getClimbingGym().getId())
                .difficulty(difficultyMapping.getDifficulty())
                .gymDifficultyName(difficultyMapping.getGymDifficultyName())
                .gymDifficultyColor(difficultyMapping.getGymDifficultyColor())
                .percentage(percentage)
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AcceptedClimbingGymSimpleResponseWithFollow {

        private Long gymId;
        private String name;
        private Long managerId;
        private Long follower;
        private String profileImageUrl;
        private Boolean isFollow;

        public static AcceptedClimbingGymSimpleResponseWithFollow toDTO(ClimbingGym climbingGym,
            Long managerId, Long follower, String profileImageUrl, boolean isFollow) {

            return AcceptedClimbingGymSimpleResponseWithFollow.builder()
                .gymId(climbingGym.getId())
                .name(climbingGym.getName())
                .managerId(managerId)
                .follower(follower)
                .profileImageUrl(profileImageUrl)
                .isFollow(isFollow)
                .build();
        }

    }

}
