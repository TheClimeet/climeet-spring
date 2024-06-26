package com.climeet.climeet_backend.domain.sector;

import com.climeet.climeet_backend.domain.sector.dto.SectorResponseDto.SectorDetailResponse;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class SectorService {
    private final SectorRepository sectorRepository;

    public List<SectorDetailResponse> getSectorList(Long gymId) {
        List<Sector> sectorList = sectorRepository.findSectorByClimbingGymId(gymId);
        if (sectorList.isEmpty()) {
            throw new GeneralException(ErrorStatus._EMPTY_SECTOR_LIST);
        }

        return sectorList.stream()
            .map(SectorDetailResponse::toDTO)
            .toList();
    }
}