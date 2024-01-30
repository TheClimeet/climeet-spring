package com.climeet.climeet_backend.domain.difficultymapping;

import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingRequestDto.CreateDifficultyMappingRequest;
import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingResponseDto.DifficultyMappingDetailResponse;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "DifficultyMapping", description = "암장 난이도 매핑 ")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/gym")
public class DifficultyMappingController {

    private final DifficultyMappingService difficultyMappingService;

    @Operation(summary = "클밋, 암장 난이도 매핑 생성")
    @PostMapping("/difficulty")
    public ResponseEntity<List<Long>> createDifficultyMapping(
        @CurrentUser User user,
        @RequestBody CreateDifficultyMappingRequest createDifficultyMappingRequest
    ) {
        return ResponseEntity.ok(
            difficultyMappingService.createDifficultyMapping(user, createDifficultyMappingRequest));
    }

    @Operation(summary = "클밋, 암장 난이도 매핑 조회")
    @GetMapping("/difficulty")
    public ResponseEntity<List<DifficultyMappingDetailResponse>> getDifficultyMappingList(
        @CurrentUser User user, @RequestParam Long gymId
    ) {
        return ResponseEntity.ok(difficultyMappingService.getDifficultyMapping(user, gymId));
    }

}
