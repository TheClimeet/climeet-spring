package com.climeet.climeet_backend.domain.shorts.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
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
        private ShortsDetailInfo shortsDetailInfo;

        public static ShortsSimpleInfo toDTO(Long shortsId, String thumbnailImageUrl,
            String gymName, ShortsDetailInfo shortsDetailInfo, DifficultyMapping difficultyMapping) {
            return ShortsSimpleInfo.builder()
                .shortsId(shortsId)
                .thumbnailImageUrl(thumbnailImageUrl)
                .gymName(gymName)
                .gymDifficultyName(difficultyMapping.getGymDifficultyName())
                .gymDifficultyColor(difficultyMapping.getGymDifficultyColor())
                .climeetDifficultyName(difficultyMapping.getClimeetDifficultyName())
                .shortsDetailInfo(shortsDetailInfo)
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
        private boolean isLiked;
        private boolean isBookmarked;
        private String description;
        private String routeImageUrl;
        private String gymDifficultyName;
        private String gymDifficultyColor;
        private Boolean isSoundEnabled;

        public static ShortsDetailInfo toDTO(User user, Shorts shorts, ClimbingGym climbingGym,
            Sector sector, boolean isLiked, boolean isBookmarked, DifficultyMapping difficultyMapping) {
            return ShortsDetailInfo.builder()
                .userShortsSimpleInfo(UserShortsSimpleInfo.toDto(user))
                .shortsId(shorts.getId())
                .gymName(climbingGym.getName())
                .sectorName(sector.getSectorName())
                .gymId(climbingGym.getId())
                .sectorId(sector.getId())
                .videoUrl(shorts.getVideoUrl())
                .likeCount(shorts.getLikeCount())
                .commentCount(shorts.getCommentCount())
                .bookmarkCount(shorts.getBookmarkCount())
                .shareCount(shorts.getShareCount())
                .isLiked(isLiked)
                .isBookmarked(isBookmarked)
                .description(shorts.getDescription())
                .gymDifficultyName(difficultyMapping.getGymDifficultyName())
                .gymDifficultyColor(difficultyMapping.getGymDifficultyColor())
                .isSoundEnabled(shorts.getIsSoundEnabled())
                .build();
        }
    }
}