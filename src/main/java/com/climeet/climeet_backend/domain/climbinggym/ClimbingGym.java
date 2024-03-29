package com.climeet.climeet_backend.domain.climbinggym;

import com.climeet.climeet_backend.domain.climbinggymimage.ClimbingGymBackgroundImage;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ClimbingGym extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "climbingGym", cascade = CascadeType.ALL)
    private Manager manager;

    @OneToMany(mappedBy = "climbingGym")
    private List<ClimbingGymBackgroundImage> backgroundImageList;

    @NotNull
    private String name;

    private String profileImageUrl;

    public void updateProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    private Float AverageRating = 0.0F;

    private Float sumRating = 0.0F;

    private int reviewCount = 0;

    private String tel;

    private String address;

    private String location;

    @Column(columnDefinition = "json")
    private String businessHours;

    @Column(columnDefinition = "json")
    private String priceList;

    public void updateGymInfo(String tel, String address, String businessHours) {
        this.tel = tel;
        this.address = address;
        this.businessHours = businessHours;
    }

    public void updateGymPriceList(String priceList) {
        this.priceList = priceList;
    }


    public void reviewCreate(Float rating) {
        this.sumRating += rating;
        this.reviewCount++;
        this.averageRatingCalculate();
    }

    public void reviewChange(Float beforeRating, Float afterRating) {
        this.sumRating -= beforeRating;
        this.sumRating += afterRating;
        this.averageRatingCalculate();
    }

    public void averageRatingCalculate() {
        if (this.reviewCount > 0) {
            float averageRating = this.sumRating / this.reviewCount;
            DecimalFormat decimalFormat = new DecimalFormat("#.0");
            decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            String formattedAverageRating = decimalFormat.format(averageRating);
            this.AverageRating = Float.parseFloat(formattedAverageRating);
        } else {
            this.AverageRating = 0.0F;
        }
    }


    private int selectionCount = 0;

    private int serviceBitMask = 0;

    public void updateServiceBitMask(int value) {
        this.serviceBitMask = value;
    }

    public void setManager(Manager manager) {
        // 기존 Manager와의 관계를 해제
        if (this.manager != null) {
            this.manager.setClimbingGym(null);
        }
        this.manager = manager;
        // 새로운 Manager와의 양방향 관계를 설정
        if (manager != null) {
            manager.setClimbingGym(this);
        }
    }

    private int thisWeekFollowCount = 0;

    private int thisWeekSelectionCount = 0;

    public void thisWeekFollowCountUp() {
        this.thisWeekFollowCount++;
    }

    public void thisWeekFollowCountDown() {
        this.thisWeekFollowCount--;
    }

    public void thisWeekSelectionCountUp() {
        this.thisWeekSelectionCount++;
    }

    public void thisWeekSelectionCountDown() {
        this.thisWeekSelectionCount--;

    }

}