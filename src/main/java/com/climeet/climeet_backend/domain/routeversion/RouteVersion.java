package com.climeet.climeet_backend.domain.routeversion;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggymlayoutimage.ClimbingGymLayoutImage;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import io.swagger.v3.core.util.Json;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RouteVersion extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private ClimbingGym climbingGym;

    @NotNull
    private LocalDate timePoint;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<Long> difficultyMappingList;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private List<Long> layoutList;

    @Type(JsonType.class)
    @Column(columnDefinition = "json")
    private Map<String, List<Long>> climbData;


    public static RouteVersion toEntity(ClimbingGym climbingGym, LocalDate timePoint,
        List<Long> difficultyMappingList, List<Long> layoutList ,Map<String, List<Long>> climbData) {
        return RouteVersion.builder()
            .climbingGym(climbingGym)
            .timePoint(timePoint)
            .difficultyMappingList(difficultyMappingList)
            .layoutList(layoutList)
            .climbData(climbData)
            .build();
    }
}