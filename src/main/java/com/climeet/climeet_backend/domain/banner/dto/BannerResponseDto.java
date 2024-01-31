package com.climeet.climeet_backend.domain.banner.dto;

import com.climeet.climeet_backend.domain.banner.Banner;
import com.climeet.climeet_backend.domain.bestclimber.clear.BestClearClimber;
import com.climeet.climeet_backend.domain.bestclimber.clear.dto.BestClearClimberResponseDto;
import java.time.LocalDate;
import java.util.PrimitiveIterator;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class BannerResponseDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class BannerDetailInfo{

        private Long id;
        private String bannerImageUrl;
        private String title;
        private String bannerTargetUrl;
        private LocalDate bannerStartDate;
        private LocalDate bannerEndDate;
        private Boolean isPopup;
        private String linkUrl;


        public static BannerDetailInfo toDTO(
            Banner banner){

            return BannerDetailInfo.builder()
                .id(banner.getId())
                .bannerImageUrl(banner.getBannerImageUrl())
                .title(banner.getTitle())
                .bannerTargetUrl(banner.getBannerTargetUrl())
                .bannerStartDate(banner.getBannerStartDate())
                .bannerEndDate(banner.getBannerEndDate())
                .isPopup(banner.getIsPopup())
                .linkUrl(banner.getLinkUrl())
                .build();
        }
    }
}
