package com.climeet.climeet_backend.domain.bestroute;

import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
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
public class BestRoute extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    private Route route;

    private int ranking;

    private String routeImageUrl;

    private int thisWeekSelectionCount;

    private String gymName;

    private String sectorName;

    private String climeetDifficultyName;

    private String gymDifficultyName;

    private String gymDifficultyColor;


}