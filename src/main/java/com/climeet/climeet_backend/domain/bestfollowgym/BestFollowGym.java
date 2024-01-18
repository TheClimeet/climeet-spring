package com.climeet.climeet_backend.domain.bestfollowgym;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class BestFollowGym {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private ClimbingGym climbingGym;

    private int ranking = 0;

    private int thisWeekFollowCount = 0;

    private String profileImageUrl;

    private String gymName;

    private Float rating;

    private int reviewCount=0;
}
