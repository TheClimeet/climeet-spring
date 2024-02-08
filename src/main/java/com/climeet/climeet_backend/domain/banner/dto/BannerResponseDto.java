package com.climeet.climeet_backend.domain.banner.dto;

import com.climeet.climeet_backend.domain.banner.Banner;
import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BannerResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BannerDetailInfo {

        private Long id;
        private String bannerImageUrl;
        private String title;
        private String bannerTargetUrl;
        private LocalDate bannerStartDate;
        private LocalDate bannerEndDate;
        private Boolean isPopup;
        private String linkUrl;


        public static BannerDetailInfo toDTO(
            Banner banner) {

            return BannerDetailInfo.builder()
                .id(banner.getId())
                .bannerImageUrl(banner.getBannerImageUrl())
                .title(banner.getTitle())
                .bannerTargetUrl(banner.getBannerTargetUrl())
                .bannerStartDate(banner.getBannerStartDate())
                .bannerEndDate(banner.getBannerEndDate())
                .isPopup(banner.getIsPopup())
                .build();
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BannerSimpleInfo {

        private Long id;
        private String title;
        private LocalDate bannerStartDate;
        private LocalDate bannerEndDate;
        private Boolean isPopup;
        private LocalDateTime createdAt;
        private String status;

        public static BannerSimpleInfo toDTO(
            Banner banner, String status) {

            return BannerSimpleInfo.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .bannerStartDate(banner.getBannerStartDate())
                .bannerEndDate(banner.getBannerEndDate())
                .isPopup(banner.getIsPopup())
                .createdAt(banner.getCreatedAt())
                .status(status)
                .build();
        }
    }
}
