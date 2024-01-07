package com.climeet.climeet_backend.domain.shorts;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.route.RouteRepository;
import com.climeet.climeet_backend.domain.sector.Sector;
import com.climeet.climeet_backend.domain.sector.SectorRepository;
import com.climeet.climeet_backend.domain.shorts.dto.ShortsRequestDto.PostShortsReq;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.s3.S3Service;
import lombok.RequiredArgsConstructor;
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
}