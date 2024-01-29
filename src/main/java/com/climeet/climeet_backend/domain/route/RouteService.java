package com.climeet.climeet_backend.domain.route;

import com.climeet.climeet_backend.domain.route.dto.RouteRequestDto.CreateRouteRequest;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteDetailResponse;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteSimpleResponse;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.domain.sector.SectorRepository;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.s3.S3Service;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final RouteRepository routeRepository;
    private final SectorRepository sectorRepository;
    private final S3Service s3Service;

    @Transactional
    public RouteSimpleResponse createRoute(CreateRouteRequest createRouteRequest,
        MultipartFile routeImage) {

        Sector sector = sectorRepository.findById(createRouteRequest.getSectorId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SECTOR));

        String routeImageUrl = s3Service.uploadFile(routeImage).getImgUrl();

        routeRepository.save(Route.toEntity(createRouteRequest, sector, routeImageUrl));

        // Route Id 값을 반환하기 위해 사용
        Route route = routeRepository.findByRouteImageUrl(routeImageUrl);

        return RouteSimpleResponse.toDto(route);
    }

    public RouteDetailResponse getRoute(Long routeId) {
        Route route = routeRepository.findById(routeId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_ROUTE));

        return RouteDetailResponse.toDto(route);
    }

    public List<RouteDetailResponse> getRouteList(Long gymId) {
        List<Route> routeList = routeRepository.findBySectorClimbingGymId(gymId);
        if (routeList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_ROUTE_LIST);
        }
        return routeList.stream()
            .map(RouteDetailResponse::toDto)
            .collect(Collectors.toList());
    }
}