package com.climeet.climeet_backend.domain.route;

import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMappingRepository;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.domain.route.dto.RouteRequestDto.CreateRouteRequest;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteDetailResponse;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteSimpleResponse;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.domain.sector.SectorRepository;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.s3.S3Service;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RouteService {

    private final ManagerRepository managerRepository;
    private final DifficultyMappingRepository difficultyMappingRepository;
    private final RouteRepository routeRepository;
    private final SectorRepository sectorRepository;
    private final S3Service s3Service;

    @Transactional
    public RouteSimpleResponse createRoute(CreateRouteRequest createRouteRequest,
        MultipartFile routeImage, User user) {

        Manager manager = managerRepository.findById(user.getId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MANAGER));

        DifficultyMapping difficultyMapping = difficultyMappingRepository.findByClimbingGymAndGymDifficultyName(
            manager.getClimbingGym(), createRouteRequest.getGymDifficultyName());

        Sector sector = sectorRepository.findById(createRouteRequest.getSectorId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SECTOR));

        String routeImageUrl = s3Service.uploadFile(routeImage).getImgUrl();

        Route route = routeRepository.save(
            Route.toEntity(sector, routeImageUrl, difficultyMapping,
                createRouteRequest.getHoldColor()));

        return RouteSimpleResponse.toDTO(route);
    }

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