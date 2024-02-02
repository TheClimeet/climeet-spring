package com.climeet.climeet_backend.domain.banner;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    @Query("SELECT b "
        + "FROM Banner b "
        + "WHERE :currentDate >= b.bannerStartDate AND :currentDate <= b.bannerEndDate")
    List<Banner> findByBannerStartDateBeforeAndBannerEndDateAfter(LocalDate currentDate);

}