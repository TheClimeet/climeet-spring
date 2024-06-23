package com.climeet.climeet_backend.domain.difficultymapping;

import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingRequestDto.CreateDifficultyMappingRequest;
import com.climeet.climeet_backend.domain.difficultymapping.dto.DifficultyMappingResponseDto.DifficultyMappingDetailResponse;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.security.CurrentUser;
import com.climeet.climeet_backend.global.utils.SwaggerApiError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "1500 - DifficultyMapping", description = "암장 난이도 매핑 ")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/gyms")
public class DifficultyMappingController {

    private final DifficultyMappingService difficultyMappingService;

    @Operation(summary = "클밋, 암장 난이도 매핑 생성 - 1501 [무빗]")
    @SwaggerApiError({ErrorStatus._INVALID_DIFFICULTY, ErrorStatus._EMPTY_MANAGER})
    @PostMapping("/difficulty")
    public ResponseEntity<List<Long>> createDifficultyMapping(@CurrentUser User user,
        @RequestBody CreateDifficultyMappingRequest createDifficultyMappingRequest
    ) {
        return ResponseEntity.ok(
            difficultyMappingService.createDifficultyMapping(user, createDifficultyMappingRequest));
    }

    @Operation(summary = "클밋, 암장 난이도 매핑 조회 - 1502 [무빗]")
    @SwaggerApiError({ErrorStatus._INVALID_DIFFICULTY, ErrorStatus._EMPTY_CLIMBING_GYM,
        ErrorStatus._EMPTY_DIFFICULTY_LIST})
    @GetMapping("/{gymId}/difficulty")
    public ResponseEntity<List<DifficultyMappingDetailResponse>> getDifficultyMappingList(
        @CurrentUser User user, @PathVariable Long gymId
    ) {
        return ResponseEntity.ok(difficultyMappingService.getDifficultyMapping(gymId));
    }

    @Operation(summary = "암장 색 코드 목록 조회 - 1503 [무빗]")
    @SwaggerApiError({})
    @GetMapping("/difficulty/color")
    public ResponseEntity<Map<String, String>> getGymDifficultyColorList(
        @CurrentUser User user
    ) {
        return ResponseEntity.ok(difficultyMappingService.getGymDifficultyColorList());
    }

}
