package com.climeet.climeet_backend.domain.user.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
import com.climeet.climeet_backend.domain.followrelationship.FollowRelationshipRepository;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.user.User;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class UserResponseDto {
    @Data
    @Getter
    public static class UserTokenSimpleInfo{
        private String accessToken;
        private String refreshToken;

        public UserTokenSimpleInfo(User user){

            this.accessToken = user.getAccessToken();
            this.refreshToken = user.getRefreshToken();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserShortsSimpleInfo{
        private Long userId;
        private String profileImgUrl;
        private String profileName;

        public static UserShortsSimpleInfo toDto(User user) {
            return UserShortsSimpleInfo.builder()
                .profileImgUrl(user.getProfileImageUrl())
                .profileName(user.getProfileName())
                .userId(user.getId())
                .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserFollowDetailInfo{
        private Long userId;
        private String userName;
        private String userProfileUrl;
        private Long followerCount;
        private Long followingCount;
        private Boolean isFollower;

        public static UserFollowDetailInfo toDTO(User follower, Boolean followStatus){
            return UserFollowDetailInfo.builder()
                .userId(follower.getId())
                .userName(follower.getProfileName())
                .userProfileUrl(follower.getProfileImageUrl())
                .followerCount(follower.getFollowerCount())
                .followingCount(follower.getFollowingCount())
                .isFollower(followStatus)
                .build();
        }

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserFollowSimpleInfo{
        private Long userId;
        private String userName;
        private String userProfileUrl;
        private Long followerCount;
        private Long followingCount;

        public static UserFollowSimpleInfo toDTO(User follower){
            return UserFollowSimpleInfo.builder()
                .userId(follower.getId())
                .userName(follower.getProfileName())
                .userProfileUrl(follower.getProfileImageUrl())
                .followerCount(follower.getFollowerCount())
                .followingCount(follower.getFollowingCount())
                .build();
        }

    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserHomeGymSimpleInfo{
        private Long gymId;
        private String gymProfileUrl;
        private String gymName;

        public static UserHomeGymSimpleInfo toDTO(ClimbingGym gym){
            return UserHomeGymSimpleInfo.builder()
                .gymId(gym.getId())
                .gymProfileUrl(gym.getProfileImageUrl())
                .gymName(gym.getName())
                .build();
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class UserHomeGymDetailInfo{
        private Long gymId;
        private String gymProfileUrl;
        private String gymName;

        @Getter
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        public static class RouteSimpleInfo {
            private Long routeId;
            private String routeImgUrl;
            private String difficultyName;
        }

        private List<RouteSimpleInfo> routeSimpleInfos;

        public static UserHomeGymDetailInfo toDTO(ClimbingGym gym, List<Route> routeList){
            List<RouteSimpleInfo> routeInfo = routeList.stream()
                .map(route -> RouteSimpleInfo.builder()
                    .routeId(route.getId())
                    .routeImgUrl(route.getRouteImageUrl())
                    .difficultyName(route.getDifficultyMapping().getClimeetDifficultyName())
                    .build())
                .toList();
            return UserHomeGymDetailInfo.builder()
                .gymId(gym.getId())
                .gymProfileUrl(gym.getProfileImageUrl())
                .gymName(gym.getName())
                .routeSimpleInfos(routeInfo)
                .build();
        }
    }
}
