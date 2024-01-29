package com.climeet.climeet_backend.domain.routeversion;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.route.RouteRepository;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteDetailResponse;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionRequestDto.CreateRouteVersionRequest;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionResponseDto.RouteVersionDetailResponse;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.domain.sector.SectorRepository;
import com.climeet.climeet_backend.domain.sector.dto.SectorResponseDto.SectorDetailResponse;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RouteVersionService {

    private final ClimbingGymRepository climbingGymRepository;
    private final RouteVersionRepository routeVersionRepository;
    private final RouteRepository routeRepository;
    private final SectorRepository sectorRepository;
    private final ManagerRepository managerRepository;

    public List<LocalDate> getRouteVersionList(Long gymId) {
        ClimbingGym climbingGym = climbingGymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));
        List<RouteVersion> routeVersionList = routeVersionRepository.findByClimbingGymOrderByTimePointDesc(
            climbingGym);
        if (routeVersionList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_VERSION_LIST);
        }
        return routeVersionList.stream()
            .map(routeVersion -> routeVersion.getTimePoint())
            .toList();
    }

    public void createRouteVersion(CreateRouteVersionRequest createRouteVersionRequest, User user) {
        Manager manager = managerRepository.findById(user.getId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MANAGER));

        Optional<RouteVersion> routeVersionOptional = routeVersionRepository.findByClimbingGymAndTimePoint(
            manager.getClimbingGym(), createRouteVersionRequest.getTimePoint());
        if (routeVersionOptional.isPresent()) {
            throw new GeneralException(ErrorStatus._DUPLICATE_ROUTE_VERSION);
        }

        List<Route> routeList = routeRepository.findByIdIn(
            createRouteVersionRequest.getRouteIdList());
        if (routeList.size() != createRouteVersionRequest.getRouteIdList().size()) {
            throw new GeneralException(ErrorStatus._MISMATCH_ROUTE_IDS);
        }

        List<Sector> sectorList = sectorRepository.findByIdIn(
            createRouteVersionRequest.getSectorIdList());
        if (sectorList.size() != createRouteVersionRequest.getSectorIdList().size()) {
            throw new GeneralException(ErrorStatus._MISMATCH_SECTOR_IDS);
        }

        String routeIdListString = RouteVersionConverter.convertListToString(
            createRouteVersionRequest.getRouteIdList());
        String sectorIdListString = RouteVersionConverter.convertListToString(
            createRouteVersionRequest.getSectorIdList());

        routeVersionRepository.save(
            RouteVersion.toEntity(manager.getClimbingGym(), createRouteVersionRequest.getTimePoint(),
                routeIdListString, sectorIdListString));

    }



    public RouteVersionDetailResponse getRouteVersionDetail(Long gymId, LocalDate timePoint) {
        ClimbingGym climbingGym = climbingGymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        RouteVersion routeVersion = routeVersionRepository.findByClimbingGymAndTimePoint(
                climbingGym, timePoint)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_VERSION));

        List<Long> routeIdList = RouteVersionConverter.convertStringToList(
            routeVersion.getRouteList());
        if (routeIdList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_ROUTE_LIST);
        }

        List<Long> sectorIdList = RouteVersionConverter.convertStringToList(
            routeVersion.getSectorList());
        if (sectorIdList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_SECTOR_LIST);
        }

        List<Route> routeList = routeRepository.findByIdIn(routeIdList);
        if (routeList.size() != routeIdList.size()) {
            throw new GeneralException(ErrorStatus._MISMATCH_ROUTE_IDS);
        }

        List<Sector> sectorList = sectorRepository.findByIdIn(sectorIdList);
        if (sectorList.size() != sectorIdList.size()) {
            throw new GeneralException(ErrorStatus._MISMATCH_SECTOR_IDS);
        }

        List<RouteDetailResponse> routeListResponse = routeList.stream()
            .map(route -> RouteDetailResponse.toDto(route)).toList();
        List<SectorDetailResponse> sectorDetailResponses = sectorList.stream()
            .map(sector -> SectorDetailResponse.toDto(sector)).toList();

        return RouteVersionDetailResponse.toDto(climbingGym, sectorDetailResponses,
            routeListResponse);
    }

}