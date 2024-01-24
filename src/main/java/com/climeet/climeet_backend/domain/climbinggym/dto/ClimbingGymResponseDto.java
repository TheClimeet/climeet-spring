package com.climeet.climeet_backend.domain.climbinggym.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.route.Route;
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
            if(climbingGym.getManager() != null){
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

        public static AcceptedClimbingGymSimpleResponse toDTO(ClimbingGym climbingGym) {
            Long managerId = null;
            Long follower = 0L;
            String profileImageUrl = null;
            if(climbingGym.getManager() != null){
                managerId = climbingGym.getManager().getId();
                if(climbingGym.getManager().getFollowerCount() != null){
                    follower = climbingGym.getManager().getFollowerCount();
                }
            }
            if(climbingGym.getProfileImageUrl() != null){
                profileImageUrl = climbingGym.getProfileImageUrl();
            }

            return AcceptedClimbingGymSimpleResponse.builder()
                .id(climbingGym.getId())
                .name(climbingGym.getName())
                .managerId(managerId)
                .follower(follower)
                .profileImageUrl(profileImageUrl)
                .build();
        }

    }

}
