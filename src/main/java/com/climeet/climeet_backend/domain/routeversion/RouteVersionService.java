package com.climeet.climeet_backend.domain.routeversion;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.climbinggymlayoutimage.ClimbingGymLayoutImage;
import com.climeet.climeet_backend.domain.climbinggymlayoutimage.ClimbingGymLayoutImageRepository;
import com.climeet.climeet_backend.domain.climbinggymlayoutimage.dto.ClimbingGymLayoutImageResponseDto.LayoutImgListDetail;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMappingRepository;
import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingResponseDto.DifficultyMappingDetailResponse;
import com.climeet.climeet_backend.domain.difficultymapping.enums.ClimeetDifficulty;
import com.climeet.climeet_backend.domain.difficultymapping.enums.GymDifficulty;
import com.climeet.climeet_backend.domain.fcmNotification.FcmNotificationService;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.route.RouteRepository;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteDetailResponse;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionRequestDto.CreateRouteVersionRequest;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionRequestDto.GetFilteredRouteVersionRequest;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionResponseDto.RouteVersionAllDataResponse;
import com.climeet.climeet_backend.domain.routeversion.dto.RouteVersionResponseDto.RouteVersionFilteringKeyResponse;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.domain.sector.SectorRepository;
import com.climeet.climeet_backend.domain.sector.dto.SectorResponseDto.SectorDetailResponse;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final DifficultyMappingRepository difficultyMappingRepository;
    private final ClimbingGymLayoutImageRepository climbingGymLayoutImageRepository;
    private final FcmNotificationService fcmNotificationService;

    public List<LocalDate> getRouteVersionList(Long gymId) {
        List<LocalDate> timePointList = routeVersionRepository.findTimePointListByGymId(gymId);
        if (timePointList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_VERSION_LIST);
        }
        return timePointList;
    }

    public void createRouteVersion(CreateRouteVersionRequest requestDto, User user) {
        // user가 매니저인지 확인
        Manager manager = managerRepository.findById(user.getId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MANAGER));

        // 암장 난이도 추가 변경
        List<DifficultyMapping> difficultyMappingList = difficultyMappingRepository.findByClimbingGymOrderByDifficultyAsc(
            manager.getClimbingGym());
        List<Long> filteredDifficultyMappingIdList = new ArrayList<>(difficultyMappingList.stream()
            .filter(difficulty -> requestDto.getExistingData().getDifficulty()
                .contains(difficulty.getGymDifficultyName()))
            .map(DifficultyMapping::getId)
            .toList());
        List<Long> newDifficultyImageIdList = requestDto.getNewData().getDifficulty().stream()
            .map(difficultyDto -> {
                DifficultyMapping targetDifficulty = difficultyMappingList.stream()
                    .filter(difficulty -> difficulty.getGymDifficultyName()
                        .equals(difficultyDto.getGymDifficultyName()))
                    .findFirst()
                    .orElse(null);
                if (targetDifficulty != null) {
                    targetDifficulty.changeDifficultyMapping(ClimeetDifficulty.findByString(
                        difficultyDto.getClimeetDifficultyName()));
                } else {
                    targetDifficulty = DifficultyMapping.toEntity(
                        GymDifficulty.findByName(difficultyDto.getGymDifficultyName()),
                        ClimeetDifficulty.findByString(difficultyDto.getClimeetDifficultyName()),
                        manager.getClimbingGym());
                }
                return difficultyMappingRepository.save(targetDifficulty).getId();
            })
            .toList();
        filteredDifficultyMappingIdList.addAll(newDifficultyImageIdList);

        // 암장 층별 이미지 추가
        List<ClimbingGymLayoutImage> layoutImageList = climbingGymLayoutImageRepository.findClimbingGymLayoutImageByClimbingGym(
            manager.getClimbingGym());
        List<Long> filteredLayoutImageIdList = new ArrayList<>(layoutImageList.stream()
            .filter(layout -> requestDto.getExistingData().getLayout().contains(layout.getFloor()))
            .map(ClimbingGymLayoutImage::getId)
            .toList());
        List<Long> newLayoutImageIdList = requestDto.getNewData().getLayout().stream()
            .map(layoutDto -> {
                ClimbingGymLayoutImage targetLayoutImage = layoutImageList.stream()
                    .filter(layout -> layout.getFloor() == layoutDto.getFloor())
                    .findFirst()
                    .orElse(null);
                if (targetLayoutImage != null) {
                    targetLayoutImage.changeImgUrl(layoutDto.getImgUrl());
                } else {
                    targetLayoutImage = ClimbingGymLayoutImage.toEntity(
                        manager.getClimbingGym(), layoutDto.getFloor(), layoutDto.getImgUrl());
                }
                return climbingGymLayoutImageRepository.save(targetLayoutImage).getId();
            })
            .toList();
        filteredLayoutImageIdList.addAll(newLayoutImageIdList);

        // 기존 Sector 가져오기
        List<Sector> sectorList = new ArrayList<>(
            sectorRepository.findByIdIn(requestDto.getExistingData().getSector()));
        // 새 Sector 추가하기
        List<Sector> newSectorList = requestDto.getNewData().getSector().stream()
            .map(sectorDto -> {
                return sectorRepository.save(
                    Sector.toEntity(manager.getClimbingGym(), sectorDto.getName(),
                        sectorDto.getFloor(),
                        sectorDto.getImgUrl()));
            })
            .toList();
        // Sector 데이터 병합(Route 추가시에 사용)
        sectorList.addAll(newSectorList);

        // 기존 Route 가져오기
        List<Route> routeList = new ArrayList<>(
            routeRepository.findByIdIn(requestDto.getExistingData().getRoute()));
        // 새 Route 추가하기
        List<Route> newRouteList = requestDto.getNewData().getRoute().stream()
            .map(routeDto -> {
                Sector targetSector = sectorList.stream()
                    .filter(sector -> sector.getSectorName().equals(routeDto.getSectorName()))
                    .findFirst()
                    .orElseThrow(() -> new GeneralException(ErrorStatus._MISMATCH_SECTOR_DATA));
                DifficultyMapping targetDifficulty = difficultyMappingList.stream()
                    .filter(difficultyMapping -> difficultyMapping.getGymDifficultyName()
                        .equals(routeDto.getGymDifficultyName()))
                    .findFirst()
                    .orElseThrow(() -> new GeneralException(ErrorStatus._MISMATCH_DIFFICULTY_DATA));
                return routeRepository.save(
                    Route.toEntity(targetSector, targetDifficulty, routeDto.getImgUrl(),
                        routeDto.getHoldColor()));
            })
            .toList();
        routeList.addAll(newRouteList);

        Map<String, List<Long>> climbData = new HashMap<>();
        climbData.put("route", routeList.stream().map(Route::getId).toList());
        climbData.put("sector", sectorList.stream().map(Sector::getId).toList());

        RouteVersion routeVersion = routeVersionRepository.findByClimbingGymAndTimePoint(
                manager.getClimbingGym(), requestDto.getTimePoint())
            .orElse(null);
        if (routeVersion != null) {
            routeVersion.changeRouteVersion(filteredDifficultyMappingIdList,
                filteredLayoutImageIdList, climbData);
        } else {
            routeVersion = RouteVersion.toEntity(manager.getClimbingGym(),
                requestDto.getTimePoint(), filteredDifficultyMappingIdList,
                filteredLayoutImageIdList, climbData);
        }

        routeVersionRepository.save(routeVersion);

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

        List<ClimbingGymLayoutImage> layoutImageList = climbingGymLayoutImageRepository.findByIdIn(
            routeVersion.getLayoutList());

        List<Sector> sectorList = sectorRepository.findByIdIn(
            routeVersion.getClimbData().get("sector"));
        if (sectorList.size() != routeVersion.getClimbData().get("sector").size()) {
            throw new GeneralException(ErrorStatus._MISMATCH_SECTOR_IDS);
        }

        List<DifficultyMapping> difficultyList = difficultyMappingRepository.findByIdIn(
            routeVersion.getDifficultyMappingList());

        List<SectorDetailResponse> sectorDetailResponses = sectorList.stream()
            .map(SectorDetailResponse::toDTO).toList();
        List<DifficultyMappingDetailResponse> difficultyMappingDetailResponses = difficultyList.stream()
            .map(DifficultyMappingDetailResponse::toDTO).toList();
        int maxFloor = layoutImageList.stream()
            .mapToInt(ClimbingGymLayoutImage::getFloor).max().orElse(0);
        List<LayoutImgListDetail> layoutResponses = layoutImageList.stream()
            .map(LayoutImgListDetail::toDto).toList();

        return RouteVersionFilteringKeyResponse.toDTO(climbingGym, sectorDetailResponses,
            difficultyMappingDetailResponses, layoutResponses, maxFloor, routeVersion);
    }

    public PageResponseDto<List<RouteDetailResponse>> getRouteVersionFilteringRouteList(Long gymId,
        GetFilteredRouteVersionRequest getFilteredRouteVersionRequest) {

        ClimbingGym climbingGym = climbingGymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        Map<String, List<Long>> climbData = routeVersionRepository.findClimbDataByClimbingGymAndTimePoint(
            climbingGym, getFilteredRouteVersionRequest.getTimePoint());

        List<Route> routeList = routeRepository.findByIdIn(climbData.get("route"));
        if (routeList.size() != climbData.get("route").size()) {
            throw new GeneralException(ErrorStatus._MISMATCH_ROUTE_IDS);
        }

        // floor Filter 적용
        if (getFilteredRouteVersionRequest.getFloor() != null) {
            routeList = routeList.stream()
                .filter(route -> route.getSector().getFloor()
                    == getFilteredRouteVersionRequest.getFloor())
                .toList();
        }

        // sector Filter 적용
        if (getFilteredRouteVersionRequest.getSectorId() != null) {
            routeList = routeList.stream()
                .filter(route -> route.getSector().getId()
                    .equals(getFilteredRouteVersionRequest.getSectorId()))
                .toList();
        }

        // difficulty Filter 적용
        if (getFilteredRouteVersionRequest.getDifficulty() != null) {
            routeList = routeList.stream()
                .filter(route -> route.getDifficultyMapping().getDifficulty()
                    == getFilteredRouteVersionRequest.getDifficulty())
                .toList();
        }

        List<RouteDetailResponse> routeDetailResponseList = routeList.stream()
            .sorted(Comparator.comparing(Route::getId).reversed())
            .skip(
                (long) getFilteredRouteVersionRequest.getPage()
                    * getFilteredRouteVersionRequest.getSize())
            .limit(getFilteredRouteVersionRequest.getSize())
            .map(RouteDetailResponse::toDTO)
            .toList();

        boolean hasNextPage = (getFilteredRouteVersionRequest.getPage() + 1)
            * getFilteredRouteVersionRequest.getSize() < routeList.size();

        return new PageResponseDto<>(getFilteredRouteVersionRequest.getPage(), hasNextPage,
            routeDetailResponseList);
    }

    public List<Route> getRouteVersionRouteList(Long gymId) {

        ClimbingGym climbingGym = climbingGymRepository.findById(gymId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        RouteVersion routeVersion = routeVersionRepository.findFirstByClimbingGymAndTimePointLessThanEqualOrderByTimePointDesc(
                climbingGym, LocalDate.now())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_VERSION));

        List<Route> routeList = routeRepository.findByIdIn(
            routeVersion.getClimbData().get("route"));
        if (routeList.size() != routeVersion.getClimbData().get("route").size()) {
            throw new GeneralException(ErrorStatus._MISMATCH_ROUTE_IDS);
        }

        return routeList;
    }

    public RouteVersionAllDataResponse getRouteVersionAllData(User user, LocalDate timePoint) {
        Manager manager = managerRepository.findById(user.getId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MANAGER));

        RouteVersion routeVersion = routeVersionRepository.findFirstByClimbingGymAndTimePointLessThanEqualOrderByTimePointDesc(
                manager.getClimbingGym(), timePoint)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_VERSION));

        List<DifficultyMapping> difficultyMappingList = difficultyMappingRepository.findByIdIn(
            routeVersion.getDifficultyMappingList());
        List<DifficultyMappingDetailResponse> difficultyListDto = difficultyMappingList.stream()
            .map(DifficultyMappingDetailResponse::toDTO).toList();

        List<ClimbingGymLayoutImage> layoutImageList = climbingGymLayoutImageRepository.findByIdIn(
            routeVersion.getLayoutList());
        List<LayoutImgListDetail> layoutImgListDto = layoutImageList.stream()
            .map(LayoutImgListDetail::toDto).toList();

        List<Sector> sectorList = sectorRepository.findByIdIn(
            routeVersion.getClimbData().get("sector"));
        List<SectorDetailResponse> sectorListDto = sectorList.stream()
            .map(SectorDetailResponse::toDTO).toList();

        List<Route> routeList = routeRepository.findByIdIn(
            routeVersion.getClimbData().get("route"));
        List<RouteDetailResponse> routeListDto = routeList.stream().map(RouteDetailResponse::toDTO)
            .toList();

        int maxFloor = layoutImageList.stream()
            .mapToInt(ClimbingGymLayoutImage::getFloor).max().orElse(0);

        return RouteVersionAllDataResponse.toDTO(manager.getClimbingGym(), maxFloor, routeVersion,
            difficultyListDto, layoutImgListDto, sectorListDto, routeListDto);
    }
}

