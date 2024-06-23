package com.climeet.climeet_backend.domain.banner;

import com.climeet.climeet_backend.domain.banner.dto.BannerResponseDto.BannerDetailInfo;
import com.climeet.climeet_backend.domain.banner.dto.BannerResponseDto.BannerSimpleInfo;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@Tag(name = "2700 - Banner")

public class BannerController {
    private final BannerService bannerService;

    @GetMapping("api/banners")
    @Operation(summary = "2701 [훈]")
    public ResponseEntity<List<BannerDetailInfo>> getBannerListBetweenDates(
        @CurrentUser User user
    ){
        return ResponseEntity.ok(bannerService.getBannerListBetweenDates());
    }

    @GetMapping("retool/banners")
    public ResponseEntity<List<BannerSimpleInfo>> getBannerList() {
        return ResponseEntity.ok(bannerService.getBannerList());
    }
}