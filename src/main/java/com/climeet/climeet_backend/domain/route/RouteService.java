package com.climeet.climeet_backend.domain.route;

import com.climeet.climeet_backend.domain.route.dto.RouteRequestDto.RouteCreateRequestDto;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteGetResponseDto;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.domain.sector.SectorRepository;
import com.climeet.climeet_backend.global.response.code.BaseErrorCode;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
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
    public void createRoute(Long gymId, RouteCreateRequestDto routeCreateRequestDto) {
        Sector sector = sectorRepository.findById(routeCreateRequestDto.getSectorId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SECTOR));
        if (!sector.getClimbingGym().getId().equals(gymId)) {
            throw new GeneralException(ErrorStatus._GYM_ID_MISMATCH);
        }
        Route route = Route.builder()
            .sector(sector)
            .name(routeCreateRequestDto.getName())
            .difficulty(routeCreateRequestDto.getDifficulty())
            .routeImageUrl(routeCreateRequestDto.getRouteImageUrl())
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