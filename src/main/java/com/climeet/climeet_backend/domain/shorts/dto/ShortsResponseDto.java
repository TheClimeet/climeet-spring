package com.climeet.climeet_backend.domain.shorts.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
import com.climeet.climeet_backend.domain.followrelationship.FollowRelationship;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.domain.shorts.Shorts;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.dto.UserResponseDto.UserShortsSimpleInfo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class ShortsResponseDto {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortsSimpleInfo {

        private Long shortsId;
        private String thumbnailImageUrl;
        private String gymName;
        private String gymDifficultyName;
        private String gymDifficultyColor;
        private String climeetDifficultyName;
        private Boolean isManager;
        private ShortsDetailInfo shortsDetailInfo;

        public static ShortsSimpleInfo toDTO(Long shortsId, String thumbnailImageUrl,
            ClimbingGym climbingGym, ShortsDetailInfo shortsDetailInfo, String gymDifficultyName,
            String gymDifficultyColor, String gymClimeetDifficultyName, boolean isManager) {
            String gymName = null;
            if(climbingGym != null) gymName = climbingGym.getName();

            return ShortsSimpleInfo.builder().shortsId(shortsId)
                .thumbnailImageUrl(thumbnailImageUrl)
                .gymName(gymName)
                .gymDifficultyName(gymDifficultyName)
                .gymDifficultyColor(gymDifficultyColor)
                .climeetDifficultyName(gymClimeetDifficultyName)
                .isManager(isManager).shortsDetailInfo(shortsDetailInfo)
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortsDetailInfo {

        private UserShortsSimpleInfo userShortsSimpleInfo;
        private Long shortsId;
        private String gymName;
        private String sectorName;
        private Long gymId;
        private Long sectorId;
        private String videoUrl;
        private int likeCount;
        private int commentCount;
        private int bookmarkCount;
        private int shareCount;
        private Boolean isLiked;
        private Boolean isBookmarked;
        private String description;
        private String routeImageUrl;
        private String gymDifficultyName;
        private String gymDifficultyColor;
        private Boolean isSoundEnabled;

        public static ShortsDetailInfo toDTO(User user, Shorts shorts, ClimbingGym climbingGym,
            Sector sector, boolean isLiked, boolean isBookmarked, String gymDifficultyName,
            String gymDifficultyColor) {
            Long gymId = null;
            String gymName = null;
            Long sectorId = null;
            String sectorName = null;
            if(climbingGym != null) {
                gymId = climbingGym.getId();
                gymName = climbingGym.getName();
            }
            if(sector != null) {
                sectorId = sector.getId();
                sectorName = sector.getSectorName();
            }

            return ShortsDetailInfo.builder()
                .userShortsSimpleInfo(UserShortsSimpleInfo.toDTO(user))
                .shortsId(shorts.getId())
                .gymName(gymName)
                .sectorName(sectorName)
                .gymId(gymId)
                .sectorId(sectorId)
                .videoUrl(shorts.getVideoUrl())
                .likeCount(shorts.getLikeCount())
                .commentCount(shorts.getCommentCount())
                .bookmarkCount(shorts.getBookmarkCount())
                .shareCount(shorts.getShareCount())
                .isLiked(isLiked).isBookmarked(isBookmarked)
                .description(shorts.getDescription())
                .gymDifficultyName(gymDifficultyName)
                .gymDifficultyColor(gymDifficultyColor)
                .isSoundEnabled(shorts.getIsSoundEnabled())
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortsProfileSimpleInfo {

        private Long followingUserId;
        private String followingUserName;
        private String followingUserProfileUrl;
        private Boolean isUploadRecent;

        public static ShortsProfileSimpleInfo toDTO(User user,
            FollowRelationship followRelationship) {
            return ShortsProfileSimpleInfo.builder().followingUserId(user.getId())
                .followingUserName(user.getProfileName())
                .followingUserProfileUrl(user.getProfileImageUrl())
                .isUploadRecent(followRelationship.getIsUploadShortsRecent()).build();

        }

    }
}