package com.climeet.climeet_backend.domain.climbinggym.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.manager.Manager;
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
    public static class LayoutDetailResponse {

        private String layoutImageUrl;

        public static LayoutDetailResponse toDto(String layoutImageUrl) {
            return LayoutDetailResponse.builder()
                .layoutImageUrl(layoutImageUrl)
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ClimbingGymDetailResponse {
        private String gymProfileImageUrl;
        private String managerProfileImageUrl;
        private String gymName;
        private Long followerCount;
        private Long followingCount;
        private float averageRating;
        private int reviewCount;

        public static ClimbingGymDetailResponse toDto(ClimbingGym climbingGym, Manager manager){
            return ClimbingGymDetailResponse.builder()
                .gymProfileImageUrl(climbingGym.getProfileImageUrl())
                .managerProfileImageUrl(manager.getProfileImageUrl())
                .gymName(climbingGym.getName())
                .followerCount(manager.getFollowerCount())
                .followingCount(manager.getFollowingCount())
                .averageRating(climbingGym.getAverageRating())
                .reviewCount(climbingGym.getReviewCount())
                .build();
        }
    }

}
