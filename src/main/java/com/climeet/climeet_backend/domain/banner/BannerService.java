package com.climeet.climeet_backend.domain.banner;

import com.climeet.climeet_backend.domain.banner.dto.BannerResponseDto.BannerDetailInfo;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BannerService {
    private final BannerRepository bannerRepository;

    public List<BannerDetailInfo> getBannerListBetweenDates(){
        LocalDate curDate = LocalDate.now();
        List<Banner> bannerList = bannerRepository.findByBannerStartDateBeforeAndBannerEndDateAfter(curDate);
        return bannerList.stream()
            .map(BannerDetailInfo::toDTO)
            .collect(Collectors.toList());
    }
}