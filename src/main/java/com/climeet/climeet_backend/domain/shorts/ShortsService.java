package com.climeet.climeet_backend.domain.shorts;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMapping;
import com.climeet.climeet_backend.domain.difficultymapping.DifficultyMappingRepository;
import com.climeet.climeet_backend.domain.followrelationship.FollowRelationship;
import com.climeet.climeet_backend.domain.followrelationship.FollowRelationshipRepository;
import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.route.RouteRepository;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.domain.sector.SectorRepository;
import com.climeet.climeet_backend.domain.shorts.dto.ShortsRequestDto.CreateShortsRequest;
import com.climeet.climeet_backend.domain.shorts.dto.ShortsResponseDto.ShortsDetailInfo;
import com.climeet.climeet_backend.domain.shorts.dto.ShortsResponseDto.ShortsProfileSimpleInfo;
import com.climeet.climeet_backend.domain.shorts.dto.ShortsResponseDto.ShortsSimpleInfo;
import com.climeet.climeet_backend.domain.shortsbookmark.ShortsBookmarkRepository;
import com.climeet.climeet_backend.domain.shortslike.ShortsLikeRepository;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.domain.user.UserRepository;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.s3.S3Service;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class ShortsService {

    private final ShortsRepository shortsRepository;
    private final UserRepository userRepository;
    private final ClimbingGymRepository climbingGymRepository;
    private final SectorRepository sectorRepository;
    private final RouteRepository routeRepository;
    private final ShortsLikeRepository shortsLikeRepository;
    private final ShortsBookmarkRepository shortsBookmarkRepository;
    private final DifficultyMappingRepository difficultyMappingRepository;
    private final S3Service s3Service;
    private final FollowRelationshipRepository followRelationshipRepository;

    @Transactional
    public void uploadShorts(User user, MultipartFile video, MultipartFile thumbnailImage,
        CreateShortsRequest createShortsRequest) {

        ClimbingGym climbingGym = null;
        if (createShortsRequest.getClimbingGymId() != null) {
            climbingGym = climbingGymRepository.findById(createShortsRequest.getClimbingGymId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));
        }

        Sector sector = null;
        if (createShortsRequest.getSectorId() != null) {
            sector = sectorRepository.findById(createShortsRequest.getSectorId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SECTOR));
        }

        Route route = null;
        if (createShortsRequest.getRouteId() != null) {
            route = routeRepository.findById(createShortsRequest.getRouteId())
                .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_ROUTE));
        }

        String videoUrl = s3Service.uploadFile(video).getImgUrl();
        String thumbnailImageUrl = s3Service.uploadFile(thumbnailImage).getImgUrl();

        Shorts shorts = Shorts.toEntity(user, climbingGym, sector, route, videoUrl,
            thumbnailImageUrl, createShortsRequest);

        shortsRepository.save(shorts);
    }

    public PageResponseDto<List<ShortsSimpleInfo>> findShortsLatest(User user, Long gymId,
        Long sectorId, List<Long> routeIds, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        List<ShortsVisibility> shortsVisibilities = ShortsVisibility.getPublicAndFollowersOnlyList();

        Slice<Shorts> shortsSlice = null;

        if (routeIds == null) {
            //암장으로 필터링
            if (sectorId == null) {
                shortsSlice = shortsRepository.findAllByShortsVisibilityInAndClimbingGymIdOrderByCreatedAtDesc(
                    shortsVisibilities, gymId, pageable);
            }
            //섹터로 필터링
            if (sectorId != null) {
                shortsSlice = shortsRepository.findAllByShortsVisibilityInAndSectorIdOrderByCreatedAtDesc(
                    shortsVisibilities, sectorId, pageable);
            }
        }
        if (routeIds != null) {
            //루트 리스트로 필터링
            shortsSlice = shortsRepository.findAllByShortsVisibilityInAndRouteIdInOrderByCreatedAtDesc(
                shortsVisibilities, routeIds, pageable);
        }
        if (gymId == null) {
            shortsSlice = shortsRepository.findAllByShortsVisibilityInOrderByCreatedAtDesc(
                ShortsVisibility.getPublicAndFollowersOnlyList(), pageable);
        }

        List<ShortsSimpleInfo> shortsInfoList = shortsSlice.stream()
            //필터를 통해 팔로워만 허용한 쇼츠에서 현재 유저가 볼 수 있는지 확인
            .filter(shorts -> {
                if (shorts.getShortsVisibility() == ShortsVisibility.FOLLOWERS_ONLY) {
                    return followRelationshipRepository.existsByFollowerIdAndFollowingId(
                        user.getId(), shorts.getUser().getId());
                }
                //public이면 통과
                return true;
            }).map(shorts -> {
                DifficultyMapping difficultyMapping = difficultyMappingRepository.findByClimbingGymAndDifficulty(
                    shorts.getClimbingGym(),
                    shorts.getRoute().getDifficultyMapping().getDifficulty());

                return ShortsSimpleInfo.toDTO(shorts.getId(), shorts.getThumbnailImageUrl(),
                    shorts.getClimbingGym().getName(),
                    findShorts(user, shorts.getId(), difficultyMapping), difficultyMapping,
                    shorts.getUser() instanceof Manager);
            }).toList();

        return new PageResponseDto<>(pageable.getPageNumber(), shortsSlice.hasNext(),
            shortsInfoList);
    }

    public PageResponseDto<List<ShortsSimpleInfo>> findShortsPopular(User user, int page,
        int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Shorts> shortsSlice = shortsRepository.findAllByShortsVisibilityPublicANDByRankingNotZeroOrderByRankingAscCreatedAtDesc(
            pageable);

        List<ShortsSimpleInfo> shortsInfoList = shortsSlice.stream().map(shorts -> {
            DifficultyMapping difficultyMapping = difficultyMappingRepository.findByClimbingGymAndDifficulty(
                shorts.getClimbingGym(), shorts.getRoute().getDifficultyMapping().getDifficulty());

            return ShortsSimpleInfo.toDTO(shorts.getId(), shorts.getThumbnailImageUrl(),
                shorts.getClimbingGym().getName(),
                findShorts(user, shorts.getId(), difficultyMapping), difficultyMapping,
                shorts.getUser() instanceof Manager);
        }).toList();

        return new PageResponseDto<>(pageable.getPageNumber(), shortsSlice.hasNext(),
            shortsInfoList);
    }

    public ShortsDetailInfo findShorts(User user, Long shortsId,
        DifficultyMapping difficultyMapping) {
        Shorts shorts = shortsRepository.findById(shortsId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS));

        boolean isLiked = shortsLikeRepository.existsShortsLikeByUserAndShorts(user, shorts);
        boolean isBookmarked = shortsBookmarkRepository.existsShortsBookmarkByUserAndShorts(user,
            shorts);

        return ShortsDetailInfo.toDTO(shorts.getUser(), shorts, shorts.getClimbingGym(),
            shorts.getSector(), isLiked, isBookmarked, difficultyMapping);
    }

    public void updateShortsViewCount(User user, Long shortsId) {
        Shorts shorts = shortsRepository.findById(shortsId)
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SHORTS));

        //TODO 조회수 증가 검증 처리 추가
        shorts.updateViewCountUp();
    }

    public List<ShortsProfileSimpleInfo> getShortsProfileList(User user) {
        Long currentUserId = user.getId();
        List<FollowRelationship> followingUserList = followRelationshipRepository.findByFollowerId(
            currentUserId);
        List<ShortsProfileSimpleInfo> shortsProfileSimpleInfos = followingUserList.stream()
            .map(followRelationship -> {
                return ShortsProfileSimpleInfo.toDTO(followRelationship.getFollowing(),
                    followRelationship);
            }).toList();

        return shortsProfileSimpleInfos;
    }
}