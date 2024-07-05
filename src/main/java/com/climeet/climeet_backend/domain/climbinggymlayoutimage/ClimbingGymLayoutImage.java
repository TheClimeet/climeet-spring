package com.climeet.climeet_backend.domain.climbinggymlayoutimage;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Builder
public class ClimbingGymLayoutImage extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClimbingGym climbingGym;

    private int floor;

    private String imgUrl;

    public static ClimbingGymLayoutImage toEntity(ClimbingGym climbingGym, int floor, String imgUrl) {
        return ClimbingGymLayoutImage.builder()
            .climbingGym(climbingGym)
            .floor(floor)
            .imgUrl(imgUrl)
            .build();
    }

    public void changeImgUrl(String imgUrl){
        this.imgUrl = imgUrl;
    }

}