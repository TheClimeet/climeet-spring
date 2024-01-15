package com.climeet.climeet_backend.domain.shorts;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.route.RouteRepository;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.domain.sector.SectorRepository;
import com.climeet.climeet_backend.domain.shorts.dto.ShortsRequestDto.PostShortsReq;
import com.climeet.climeet_backend.domain.shorts.dto.ShortsResponseDto.ShortsSimpleInfo;
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
    private final ClimbingGymRepository climbingGymRepository;
    private final SectorRepository sectorRepository;
    private final RouteRepository routeRepository;
    private final S3Service s3Service;

    @Transactional
    public void uploadShorts(MultipartFile video, MultipartFile thumbnailImage,
        PostShortsReq postShortsReq) {

        ClimbingGym climbingGym = climbingGymRepository.findById(postShortsReq.getClimbingGymId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));
        Sector sector = sectorRepository.findById(postShortsReq.getSectorId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_SECTOR));
        Route route = routeRepository.findById(postShortsReq.getRouteId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_ROUTE));

        String videoUrl = s3Service.uploadFile(video).getImgUrl();
        String thumbnailImageUrl = s3Service.uploadFile(thumbnailImage).getImgUrl();

        Shorts shorts = Shorts.builder()
            .climbingGym(climbingGym)
            .route(route)
            .sector(sector)
            .videoUrl(videoUrl)
            .thumbnailImageUrl(thumbnailImageUrl)
            .description(postShortsReq.getDescription())
            .isPublic(postShortsReq.isPublic())
            .isSoundEnabled(postShortsReq.isSoundEnabled())
            .build();

        shortsRepository.save(shorts);
    }

    public PageResponseDto<List<ShortsSimpleInfo>> findShortsLatest(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Shorts> shortsSlice = shortsRepository.findAllByOrderByCreatedAtDesc(pageable);

        List<ShortsSimpleInfo> shortsInfoList = shortsSlice.stream()
            .map(shorts -> ShortsSimpleInfo.builder()
                .thumbnailImageUrl(shorts.getThumbnailImageUrl())
                .gymName(shorts.getClimbingGym().getName())
                .difficulty(shorts.getRoute().getDifficulty())
                .build())
            .toList();

        return new PageResponseDto<>(pageable.getPageNumber(), shortsSlice.hasNext(),
            shortsInfoList);
    }

    public PageResponseDto<List<ShortsSimpleInfo>> findShortsPopular(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Slice<Shorts> shortsSlice = shortsRepository.findAllByRankingNotZeroOrderByRankingAscCreatedAtDesc(pageable);

        List<ShortsSimpleInfo> shortsInfoList = shortsSlice.stream()
            .map(shorts -> ShortsSimpleInfo.builder()
                .thumbnailImageUrl(shorts.getThumbnailImageUrl())
                .gymName(shorts.getClimbingGym().getName())
                .difficulty(shorts.getRoute().getDifficulty())
                .build())
            .toList();

        return new PageResponseDto<>(pageable.getPageNumber(), shortsSlice.hasNext(),
            shortsInfoList);
    }
}