package com.climeet.climeet_backend.domain.routeversion.dto;

import java.time.LocalDate;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class RouteVersionRequestDto {

    @Getter
    @NoArgsConstructor
    public static class CreateRouteVersionRequest {

        private Long gymId;
        @DateTimeFormat(iso = ISO.DATE)
        private LocalDate date;

        private List<String> routeList;
        private List<String> sectorList;
    }

}
