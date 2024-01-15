package com.climeet.climeet_backend.domain.sector;

import com.climeet.climeet_backend.domain.climbinggym.ClimbingGym;
import com.climeet.climeet_backend.domain.climbinggym.ClimbingGymRepository;
import com.climeet.climeet_backend.domain.sector.dto.SectorRequestDto.CreateSectorRequest;
import com.climeet.climeet_backend.domain.sector.dto.SectorResponseDto.SectorDetailResponse;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class SectorService {

    private final ClimbingGymRepository climbingGymRepository;
    private final SectorRepository sectorRepository;

    @Transactional
    public void createSector(CreateSectorRequest createSectorRequest) {
        // 루트 이름 중복 체크 (같은 섹터에서 중복일 경우)
        List<Sector> sectorList = sectorRepository.findSectorByClimbingGymId(
            createSectorRequest.getGymId());
        for (Sector sector : sectorList) {
            if (sector.getSectorName().equals(createSectorRequest.getName())) {
                throw new GeneralException(ErrorStatus._DUPLICATE_SECTOR_NAME);
            }
        }

        ClimbingGym climbingGym = climbingGymRepository.findById(createSectorRequest.getGymId())
            .orElseThrow(() -> new GeneralException(ErrorStatus._EMPTY_CLIMBING_GYM));

        sectorRepository.save(Sector.toEntity(createSectorRequest, climbingGym));
    }

    public List<SectorDetailResponse> getSectorList(Long gymId) {
        List<Sector> sectorList = sectorRepository.findSectorByClimbingGymId(gymId);

        return sectorList.stream()
            .map(SectorDetailResponse::new)
            .collect(Collectors.toList());
    }
}