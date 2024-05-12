package com.climeet.climeet_backend.domain.routeversion;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.climbinggymlayoutimage.ClimbingGymLayoutImage;
import com.climeet.climeet_backend.domain.climbinggymlayoutimage.ClimbingGymLayoutImageRepository;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMappingRepository;
import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingResponseDto.DifficultyMappingDetailResponse;
import com.climeet.climeet_backend.domain.fcmNotification.FcmNotificationService;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.route.RouteRepository;
import com.climeet.climeet_backend.domain.route.dto.RouteResponseDto.RouteDetailResponse;
import com.climeet.climeet_backend.domain.routerecord.RouteRecordService;
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
    private final ClimbingGymLayoutImageRepository climbingGymLayoutImageRepository;
    private final S3Service s3Service;
    private final FcmNotificationService fcmNotificationService;

    public List<LocalDate> getRouteVersionList(Long gymId) {
        List<LocalDate> timePointList = routeVersionRepository.findTimePointListByGymId(gymId);
        if (timePointList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_VERSION_LIST);
        }
        return timePointList;
    }

    public void createRouteVersion(CreateRouteVersionRequest createRouteVersionRequest, User user,
        MultipartFile layoutImage) {
        Manager manager = managerRepository.findById(user.getId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MANAGER));

        // 암장 데이터와 TimePoint로 가장 가까운 시점의 RouteVersion 데이터를 가져옴
        // 추가하려는 날짜의 해당 암장 루트 버전 데이터가 존재하는지 확인하기 위함
        Optional<RouteVersion> routeVersionOptional = routeVersionRepository.findByClimbingGymAndTimePoint(
            manager.getClimbingGym(), createRouteVersionRequest.getTimePoint());
        if (routeVersionOptional.isPresent()) {
            throw new GeneralException(ErrorStatus._DUPLICATE_ROUTE_VERSION);
        }

        // 루트리스트에 넣을 데이터가 실제로 다 추가되어있는지 확인
        List<Route> routeList = routeRepository.findByIdIn(
            createRouteVersionRequest.getRouteIdList());
        if (routeList.size() != createRouteVersionRequest.getRouteIdList().size()) {
            throw new GeneralException(ErrorStatus._MISMATCH_ROUTE_IDS);
        }

        // 섹터리스트에 넣을 데이터가 실제로 다 추가되어있는지 확인
        List<Sector> sectorList = sectorRepository.findByIdIn(
            createRouteVersionRequest.getSectorIdList());
        if (sectorList.size() != createRouteVersionRequest.getSectorIdList().size()) {
            throw new GeneralException(ErrorStatus._MISMATCH_SECTOR_IDS);
        }

        // JSON으로 변경되면 변경 예정
        String routeIdListString = RouteVersionConverter.convertListToString(
            createRouteVersionRequest.getRouteIdList());
        String sectorIdListString = RouteVersionConverter.convertListToString(
            createRouteVersionRequest.getSectorIdList());

        // 이미지 url이나 이미지 파일이 들어오지 않았을 때 예외처리
        if (layoutImage == null && createRouteVersionRequest.getLayoutImageId() == null) {
            throw new GeneralException(ErrorStatus._EMPTY_LAYOUT_IMAGE);
        }

        ClimbingGymLayoutImage climbingGymLayoutImage = null;
        // 이미지 파일이 들어왔으면 s3에 업로드 후 사용
        if (layoutImage != null) {
            String layoutImageUrl = s3Service.uploadFile(layoutImage).getImgUrl();
            climbingGymLayoutImage = climbingGymLayoutImageRepository.save(
                ClimbingGymLayoutImage.toEntity(layoutImageUrl));
        }
        // 이미지 파일이 안들어왔으면 기존 url 그대로 사용
        else {
            climbingGymLayoutImage = climbingGymLayoutImageRepository.findById(
                    createRouteVersionRequest.getLayoutImageId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_LAYOUT_IMAGE));
        }

        routeVersionRepository.save(RouteVersion.toEntity(manager.getClimbingGym(),
            createRouteVersionRequest.getTimePoint(), routeIdListString, sectorIdListString,
            climbingGymLayoutImage));

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
            .map(SectorDetailResponse::toDTO).toList();
        List<DifficultyMappingDetailResponse> difficultyMappingDetailResponses = difficultyList.stream()
            .map(DifficultyMappingDetailResponse::toDTO).toList();
        int maxFloor = sectorList.stream()
            .mapToInt(Sector::getFloor).max().orElse(0);

        return RouteVersionFilteringKeyResponse.toDTO(climbingGym, sectorDetailResponses,
            difficultyMappingDetailResponses, maxFloor, routeVersion);
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
                    == getFilteredRouteVersionRequest.getSectorId())
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
                getFilteredRouteVersionRequest.getPage() * getFilteredRouteVersionRequest.getSize())
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

        List<Long> routeIdList = RouteVersionConverter.convertStringToList(
            routeVersion.getRouteList());
        if (routeIdList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_ROUTE_LIST);
        }

        List<Route> routeList = routeRepository.findByIdIn(routeIdList);
        if (routeList.size() != routeIdList.size()) {
            throw new GeneralException(ErrorStatus._MISMATCH_ROUTE_IDS);
        }


        return routeList;
    }
}

