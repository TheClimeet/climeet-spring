package com.climeet.climeet_backend.domain.banner;

import com.climeet.climeet_backend.domain.banner.dto.BannerResponseDto.BannerDetailInfo;
import com.climeet.climeet_backend.domain.banner.dto.BannerResponseDto.BannerSimpleInfo;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.security.CurrentUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class BannerController {
    private final BannerService bannerService;

    @GetMapping("api/banners")
    public ResponseEntity<List<BannerDetailInfo>> getBannerListBetweenDates(
        @CurrentUser User user
    ){
        return ResponseEntity.ok(bannerService.getBannerListBetweenDates());
    }

    @GetMapping("admin/banners")
    public ResponseEntity<List<BannerSimpleInfo>> getBannerList() {
        return ResponseEntity.ok(bannerService.getBannerList());
    }
}