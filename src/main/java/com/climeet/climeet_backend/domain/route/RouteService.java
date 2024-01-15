package com.climeet.climeet_backend.domain.route;

import com.climeet.climeet_backend.domain.route.dto.RouteRequestDto.CreateRouteRequest;
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
    public void createRoute(CreateRouteRequest createRouteRequest, MultipartFile routeImage) {

        // 루트 이름 중복 체크 (같은 섹터에서 중복일 경우)
        List<Route> routes = routeRepository.findBySectorId(createRouteRequest.getSectorId());
        for (Route route : routes) {
            if (route.getName().equals(createRouteRequest.getName())) {
                throw new GeneralException(ErrorStatus._DUPLICATE_ROUTE_NAME);
            }
        }

        Sector sector = sectorRepository.findById(createRouteRequest.getSectorId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SECTOR));

        String routeImageUrl = s3Service.uploadFile(routeImage).getImgUrl();
        Route route = Route.builder()
            .sector(sector)
            .name(createRouteRequest.getName())
            .difficulty(createRouteRequest.getDifficulty())
            .routeImageUrl(routeImageUrl)
            .build();

        routeRepository.save(route);
    }

    public RouteSimpleResponse getRoute(Long routeId) {
        Route route = routeRepository.findById(routeId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_ROUTE));

        return new RouteSimpleResponse(route);
    }

    public List<RouteSimpleResponse> getRouteList(Long gymId) {
        List<Route> routeList = routeRepository.findBySectorClimbingGymId(gymId);
        if (routeList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_ROUTE_LIST);
        }
        return routeList.stream()
            .map(RouteSimpleResponse::new)
            .collect(Collectors.toList());
    }
}