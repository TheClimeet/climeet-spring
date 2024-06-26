package com.climeet.climeet_backend.domain.route;

import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteDetailResponse;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.s3.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteService {
    private final RouteRepository routeRepository;
    private final S3Service s3Service;

    public RouteDetailResponse getRoute(Long routeId) {
        Route route = routeRepository.findById(routeId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_ROUTE));

        return RouteDetailResponse.toDTO(route);
    }

    public List<RouteDetailResponse> getRouteList(Long gymId) {
        List<Route> routeList = routeRepository.findBySectorClimbingGymId(gymId);
        if (routeList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_ROUTE_LIST);
        }
        return routeList.stream()
            .map(RouteDetailResponse::toDTO)
            .toList();
    }
}