package com.climeet.climeet_backend.domain.routeversion.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

public class RouteVersionRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateRouteVersionRequest {

        private Long gymId;
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate timePoint;
        private List<Long> routeIdList;
        private List<Long> sectorIdList;
    }

}
