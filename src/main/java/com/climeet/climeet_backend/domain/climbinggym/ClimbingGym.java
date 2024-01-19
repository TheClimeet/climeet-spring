package com.climeet.climeet_backend.domain.climbinggym;

import com.climeet.climeet_backend.domain.climbinggymimage.ClimbingGymBackgroundImage;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.NotNull;
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

    @OneToOne(mappedBy = "climbingGym")
    private Manager manager;

    @OneToMany(mappedBy = "climbingGym")
    private List<ClimbingGymBackgroundImage> backgroundImageList;

    @NotNull
    private String name;

    private String profileImageUrl;

    private Float AverageRating = 0.0F;

    private String layoutImageUrl;

    private int reviewCount = 0;

    private int selectionCount = 0;

    private int serviceBitMask = 0;

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

    public void thisWeekFollowCountUp(){
        this.thisWeekFollowCount++;
    }

    public void thisWeekFollowCountDown(){
        this.thisWeekFollowCount--;
    }

    public void thisWeekSelectionCountUp(){
        this.thisWeekSelectionCount++;
    }

    public void thisWeekSelectionCountDown(){
        this.thisWeekSelectionCount--;

    }
}