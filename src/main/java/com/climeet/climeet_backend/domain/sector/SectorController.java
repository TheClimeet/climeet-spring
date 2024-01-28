package com.climeet.climeet_backend.domain.sector;

import com.climeet.climeet_backend.domain.sector.dto.SectorRequestDto.CreateSectorRequest;
import com.climeet.climeet_backend.domain.sector.dto.SectorResponseDto.SectorDetailResponse;
import com.climeet.climeet_backend.domain.sector.dto.SectorResponseDto.SectorIdSimpleResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "ClimbingSector", description = "클라이밍 섹터 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("api/gym")
public class SectorController {

    private final SectorService sectorService;

    @Operation(summary = "클라이밍 섹터 생성")
    @PostMapping("/sector")
    public ResponseEntity<SectorIdSimpleResponse> createSector(
        @RequestPart(value = "image") MultipartFile sectorImage,
        @RequestPart CreateSectorRequest createSectorRequest) {

        return ResponseEntity.ok(sectorService.createSector(createSectorRequest, sectorImage));
    }

    @Operation(summary = "특정 암장 전체 섹터 조회")
    @GetMapping("/{gymId}/sector")
    public ResponseEntity<List<SectorDetailResponse>> getSectorList(
        @PathVariable Long gymId
    ) {
        List<SectorDetailResponse> sectorList = sectorService.getSectorList(gymId);
        return ResponseEntity.ok(sectorList);
    }
}