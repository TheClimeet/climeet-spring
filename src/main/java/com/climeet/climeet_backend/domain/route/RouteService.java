package com.climeet.climeet_backend.domain.route;

import com.climeet.climeet_backend.domain.route.dto.RouteRequestDto.RouteCreateRequestDto;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteGetResponseDto;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.domain.sector.SectorRepository;
import com.climeet.climeet_backend.global.response.code.BaseErrorCode;
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
    public void createRoute(Long gymId, RouteCreateRequestDto routeCreateRequestDto, MultipartFile routeImage) {
        Sector sector = sectorRepository.findById(routeCreateRequestDto.getSectorId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SECTOR));
        if (!sector.getClimbingGym().getId().equals(gymId)) {
            throw new GeneralException(ErrorStatus._GYM_ID_MISMATCH);
        }
        String routeImageUrl = s3Service.uploadFile(routeImage).getImgUrl();
        Route route = Route.builder()
            .sector(sector)
            .name(routeCreateRequestDto.getName())
            .difficulty(routeCreateRequestDto.getDifficulty())
            .routeImageUrl(routeImageUrl)
            .build();

        routeRepository.save(route);
    }

    public RouteGetResponseDto getRoute(Long routeId) {
        Route route = routeRepository.findById(routeId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_ROUTE));

        return new RouteGetResponseDto(route);
    }

    public List<RouteGetResponseDto> getRouteList(Long gymId) {
        List<Route> routeList = routeRepository.findBySectorClimbingGymId(gymId);
        if(routeList.isEmpty()){
            throw new GeneralException(ErrorStatus._EMPTY_ROUTE_LIST);
        }
        return routeList.stream()
            .map(RouteGetResponseDto::new)
            .collect(Collectors.toList());
    }
}