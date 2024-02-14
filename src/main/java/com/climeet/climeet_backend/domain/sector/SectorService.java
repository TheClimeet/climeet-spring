package com.climeet.climeet_backend.domain.sector;

import com.climeet.climeet_backend.domain.manager.Manager;
import com.climeet.climeet_backend.domain.manager.ManagerRepository;
import com.climeet.climeet_backend.domain.sector.dto.SectorRequestDto.CreateSectorRequest;
import com.climeet.climeet_backend.domain.sector.dto.SectorResponseDto.SectorDetailResponse;
import com.climeet.climeet_backend.domain.sector.dto.SectorResponseDto.SectorSimpleResponse;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import com.climeet.climeet_backend.global.s3.S3Service;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class SectorService {

    private final SectorRepository sectorRepository;
    private final ManagerRepository managerRepository;
    private final S3Service s3Service;

    @Transactional
    public SectorSimpleResponse createSector(CreateSectorRequest createSectorRequest,
        MultipartFile sectorImage, User user) {

        Manager manager = managerRepository.findById(user.getId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_MANAGER));

//        // 섹터 이름 중복 체크 (같은 섹터에서 중복일 경우)
//        List<Sector> sectorList = sectorRepository.findSectorByClimbingGymId(
//            manager.getClimbingGym().getId());
//        for (Sector sector : sectorList) {
//            if (sector.getSectorName().equals(createSectorRequest.getName())) {
//                throw new GeneralException(ErrorStatus._DUPLICATE_SECTOR_NAME);
//            }
//        }

        String sectorImageUrl = s3Service.uploadFile(sectorImage).getImgUrl();

        Sector sector = sectorRepository.save(
            Sector.toEntity(createSectorRequest, manager.getClimbingGym(), sectorImageUrl));

        return SectorSimpleResponse.toDto(sector);
    }

    public List<SectorDetailResponse> getSectorList(Long gymId) {
        List<Sector> sectorList = sectorRepository.findSectorByClimbingGymId(gymId);
        if (sectorList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_SECTOR_LIST);
        }

        return sectorList.stream()
            .map(SectorDetailResponse::toDto)
            .collect(Collectors.toList());
    }
}