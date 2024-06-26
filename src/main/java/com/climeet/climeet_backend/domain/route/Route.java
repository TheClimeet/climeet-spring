package com.climeet.climeet_backend.domain.route;

import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
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
import org.hibernate.annotations.ColumnDefault;

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
    @ManyToOne(fetch = FetchType.LAZY)
    private DifficultyMapping difficultyMapping;

    private String holdColor;

    private String routeImageUrl;


    private int thisWeekSelectionCount = 0;

    private int thisWeekShortsTagCount = 0;

    @ColumnDefault("0")
    private int selectionCount;

    public static Route toEntity(Sector sector,
        DifficultyMapping difficultyMapping, String routeImageUrl, String holdColor) {
        return Route.builder()
            .sector(sector)
            .difficultyMapping(difficultyMapping)
            .routeImageUrl(routeImageUrl)
            .holdColor(holdColor)
            .build();
    }

    public void thisWeekSelectionCountUp() {
        this.thisWeekSelectionCount++;
    }

    public void thisWeekSelectionCountDown() {
        this.thisWeekSelectionCount--;
    }
}