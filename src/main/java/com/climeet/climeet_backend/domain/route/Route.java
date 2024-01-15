package com.climeet.climeet_backend.domain.route;

import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Route extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    Sector sector;

    @NotNull
    private String name;

    @NotNull
    private int difficulty;

    private String routeImageUrl;

    private int selectionCount = 0;

    private int thisWeekShortsTagCount = 0;
}