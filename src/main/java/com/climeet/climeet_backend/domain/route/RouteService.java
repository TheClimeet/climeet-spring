package com.climeet.climeet_backend.domain.route;

import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteGetResponseDto;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.domain.sector.SectorRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final SectorRepository sectorRepository;

    @Transactional
    public void createRoute(Long gymId, Long sectorId, String name, int difficulty,
        String routeImageUrl) {
        Sector sector = sectorRepository.findById(sectorId).orElseThrow();
        Route route = Route.builder()
            .sector(sector)
            .name(name)
            .difficulty(difficulty)
            .routeImageUrl(routeImageUrl)
            .build();

        routeRepository.save(route);
    }

    public RouteGetResponseDto getRoute(Long routeId) {
        Route route = routeRepository.findById(routeId).orElseThrow();

        return new RouteGetResponseDto(route);
    }

    public List<RouteGetResponseDto> getRouteList(Long gymId) {
        List<Route> routeList = routeRepository.findBySectorClimbingGymId(gymId);

        return routeList.stream()
            .map(RouteGetResponseDto::new)
            .collect(Collectors.toList());
    }
}