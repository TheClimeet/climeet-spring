package com.climeet.climeet_backend.domain.routeversion;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMappingRepository;
import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingResponseDto.DifficultyMappingDetailResponse;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.route.RouteRepository;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteDetailResponse;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionRequestDto.CreateRouteVersionRequest;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionRequestDto.GetFilteredRouteVersionRequest;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionResponseDto.RouteVersionFilteringKeyResponse;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.domain.sector.SectorRepository;
import com.climeet.climeet_backend.domain.sector.dto.SectorResponseDto.SectorDetailResponse;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.s3.S3Service;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import java.time.LocalDate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class RouteVersionService {

    private final ClimbingGymRepository climbingGymRepository;
    private final RouteVersionRepository routeVersionRepository;
    private final RouteRepository routeRepository;
    private final SectorRepository sectorRepository;
    private final ManagerRepository managerRepository;
    private final DifficultyMappingRepository difficultyMappingRepository;
    private final S3Service s3Service;

    public List<LocalDate> getRouteVersionList(Long gymId) {
        ClimbingGym climbingGym = climbingGymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));
        List<RouteVersion> routeVersionList = routeVersionRepository.findByClimbingGymOrderByTimePointDesc(
            climbingGym);
        if (routeVersionList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_VERSION_LIST);
        }
        return routeVersionList.stream().map(RouteVersion::getTimePoint).toList();
    }

    public void createRouteVersion(CreateRouteVersionRequest createRouteVersionRequest, User user,
        MultipartFile layoutImage) {
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

        // 이미지 url이나 이미지 파일이 들어오지 않았을 때 예외처리
        if (layoutImage == null && createRouteVersionRequest.getLayoutImageUrl() == null) {
            throw new GeneralException(ErrorStatus._EMPTY_LAYOUT_IMAGE);
        }

        String layoutImageUrl;
        // 이미지 파일이 들어왔으면 s3에 업로드 후 사용
        if (layoutImage != null) {
            layoutImageUrl = s3Service.uploadFile(layoutImage).getImgUrl();
        }
        // 이미지 파일이 안들어왔으면 기존 url 그대로 사용
        else {
            layoutImageUrl = createRouteVersionRequest.getLayoutImageUrl();
        }

        routeVersionRepository.save(RouteVersion.toEntity(manager.getClimbingGym(),
            createRouteVersionRequest.getTimePoint(), routeIdListString, sectorIdListString,
            layoutImageUrl));

    }

    public RouteVersionFilteringKeyResponse getRouteVersionFilteringKey(Long gymId,
        LocalDate timePoint) {
        if (timePoint == null) {
            timePoint = LocalDate.now();
        }

        ClimbingGym climbingGym = climbingGymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        RouteVersion routeVersion = routeVersionRepository.findFirstByClimbingGymAndTimePointLessThanEqualOrderByTimePointDesc(
                climbingGym, timePoint)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_VERSION));

        List<Long> sectorIdList = RouteVersionConverter.convertStringToList(
            routeVersion.getSectorList());
        if (sectorIdList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_SECTOR_LIST);
        }

        List<Sector> sectorList = sectorRepository.findByIdIn(sectorIdList);
        if (sectorList.size() != sectorIdList.size()) {
            throw new GeneralException(ErrorStatus._MISMATCH_SECTOR_IDS);
        }

        List<DifficultyMapping> difficultyList = difficultyMappingRepository.findByClimbingGymOrderByDifficultyAsc(
            climbingGym);
        if (difficultyList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_DIFFICULTY_LIST);
        }

        List<SectorDetailResponse> sectorDetailResponses = sectorList.stream()
            .map(SectorDetailResponse::toDto).toList();
        List<DifficultyMappingDetailResponse> difficultyMappingDetailResponses = difficultyList.stream()
            .map(DifficultyMappingDetailResponse::toDto).toList();

        return RouteVersionFilteringKeyResponse.toDto(climbingGym, sectorDetailResponses,
            difficultyMappingDetailResponses, routeVersion);
    }

    public PageResponseDto<List<RouteDetailResponse>> getRouteVersionFilteringRouteList(Long gymId,
        GetFilteredRouteVersionRequest getFilteredRouteVersionRequest) {

        ClimbingGym climbingGym = climbingGymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        RouteVersion routeVersion = routeVersionRepository.findFirstByClimbingGymAndTimePointLessThanEqualOrderByTimePointDesc(
                climbingGym, getFilteredRouteVersionRequest.getTimePoint())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_VERSION));

        List<Long> routeIdList = RouteVersionConverter.convertStringToList(
            routeVersion.getRouteList());
        if (routeIdList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_ROUTE_LIST);
        }

        List<Route> routeList = routeRepository.findByIdIn(routeIdList);
        if (routeList.size() != routeIdList.size()) {
            throw new GeneralException(ErrorStatus._MISMATCH_ROUTE_IDS);
        }

        // floor Filter 적용
        if (getFilteredRouteVersionRequest.getFloorList().length != 0) {
            routeList = routeList.stream()
                .filter(route -> Arrays.stream(getFilteredRouteVersionRequest.getFloorList())
                    .anyMatch(floor -> floor == route.getSector().getFloor())
                ).toList();
        }

        // sector Filter 적용
        if (getFilteredRouteVersionRequest.getSectorIdList().length != 0) {
            routeList = routeList.stream()
                .filter(route -> Arrays.stream(getFilteredRouteVersionRequest.getSectorIdList())
                    .anyMatch(sectorId -> sectorId == route.getSector().getId())
                ).toList();
        }

        // difficulty Filter 적용
        if (getFilteredRouteVersionRequest.getDifficultyList().length != 0) {
            routeList = routeList.stream()
                .filter(route -> Arrays.stream(getFilteredRouteVersionRequest.getDifficultyList())
                    .anyMatch(
                        difficulty -> difficulty == route.getDifficultyMapping().getDifficulty())
                ).toList();
        }

        List<RouteDetailResponse> routeDetailResponseList = routeList.stream()
            .sorted(Comparator.comparing(Route::getId).reversed())
            .skip(
                getFilteredRouteVersionRequest.getPage() * getFilteredRouteVersionRequest.getSize())
            .limit(getFilteredRouteVersionRequest.getSize())
            .map(RouteDetailResponse::toDto)
            .toList();

        boolean hasNextPage = (getFilteredRouteVersionRequest.getPage() + 1)
            * getFilteredRouteVersionRequest.getSize() < routeList.size();

        return new PageResponseDto<>(getFilteredRouteVersionRequest.getPage(), hasNextPage,
            routeDetailResponseList);
    }
}

