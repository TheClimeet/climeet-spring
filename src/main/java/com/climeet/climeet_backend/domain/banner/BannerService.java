package com.climeet.climeet_backend.domain.banner;

import com.climeet.climeet_backend.domain.banner.dto.BannerResponseDto.BannerDetailInfo;
import com.climeet.climeet_backend.domain.banner.dto.BannerResponseDto.BannerSimpleInfo;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BannerService {

    private final BannerRepository bannerRepository;

    public List<BannerDetailInfo> getBannerListBetweenDates() {
        LocalDate currentDate = LocalDate.now();
        List<Banner> bannerList = bannerRepository.findByBannerStartDateBeforeAndBannerEndDateAfter(
            currentDate);
        return bannerList.stream()
            .map(BannerDetailInfo::toDTO)
            .collect(Collectors.toList());
    }

    public List<BannerSimpleInfo> getBannerList() {
        LocalDate currentDate = LocalDate.now();

        List<Banner> bannerList = bannerRepository.findAll();
        return bannerList.stream().map(banner -> {

            String status = calculateStatus(currentDate, banner.getBannerStartDate(), banner.getBannerEndDate());

            return BannerSimpleInfo.toDTO(banner, status);
        }).collect(Collectors.toList());
    }

    private String calculateStatus(LocalDate currentDate, LocalDate startDate, LocalDate endDate) {
        if (currentDate.isBefore(startDate)) {
            return "진행 예정";
        } else if (currentDate.isAfter(endDate)) {
            return "완료";
        } else {
            return "진행중";
        }
    }
}