package com.climeet.climeet_backend.domain.shorts.dto;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.domain.shorts.Shorts;
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
        private int difficulty;

        public static ShortsSimpleInfo toDTO(Long shortsId, String thumbnailImageUrl,
            String gymName,
            int difficulty) {
            return ShortsSimpleInfo.builder()
                .shortsId(shortsId)
                .thumbnailImageUrl(thumbnailImageUrl)
                .gymName(gymName)
                .difficulty(difficulty)
                .build();
        }
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ShortsDetailInfo {

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
        private Boolean isSoundEnabled;

        public static ShortsDetailInfo toDTO(Shorts shorts, ClimbingGym climbingGym,
            Sector sector, boolean isLiked, boolean isBookmarked) {
            return ShortsDetailInfo.builder()
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
                .isSoundEnabled(shorts.getIsSoundEnabled())
                .build();
        }
    }
}