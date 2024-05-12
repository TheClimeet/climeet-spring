package com.climeet.climeet_backend.domain.routeversion;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggymlayoutimage.ClimbingGymLayoutImage;
import com.climeet.climeet_backend.global.utils.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotNull.List;
import java.time.LocalDate;
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

    @ManyToOne(fetch = FetchType.LAZY)
    private ClimbingGymLayoutImage climbingGymLayoutImage;

    @NotNull
    private LocalDate timePoint;

    @NotNull
    private String routeList;

    @NotNull
    private String sectorList;

    @Type()
    @Column(columnDefinition = "json")
    private Map<String, List<Long>> climbData;


    public static RouteVersion toEntity(ClimbingGym climbingGym, LocalDate timePoint,
        String routeList, String sectorList, ClimbingGymLayoutImage climbingGymLayoutImage) {
        return RouteVersion.builder()
            .climbingGym(climbingGym)
            .climbingGymLayoutImage(climbingGymLayoutImage)
            .timePoint(timePoint)
            .routeList(routeList)
            .sectorList(sectorList)
            .build();
    }
}