package com.climeet.climeet_backend.domain.difficultymapping;

import com.climeet.climeet_backend.domain.difficultymapping.dto.difficultyMappingRequestDto.CreateDifficultyMappingRequest;
import com.climeet.climeet_backend.domain.route.dto.RouteRequestDto.CreateRouteRequest;
import com.climeet.climeet_backend.domain.user.User;
import com.climeet.climeet_backend.global.security.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;


@Tag(name = "DifficultyMapping", description = "암장 난이도 매핑 ")
@RestController
@RequiredArgsConstructor
@RequestMapping("api/gym")
public class DifficultyMappingController {

    private final DifficultyMappingService difficultyMappingService;

    @Operation(summary = "클밋, 암장 난이도 매핑")
    @PostMapping("/difficulty")
    public ResponseEntity<String> createDifficultyMapping(
        @CurrentUser User user,
        @RequestBody CreateDifficultyMappingRequest createDifficultyMappingRequest
    ) {
        difficultyMappingService.createDifficultyMapping(user, createDifficultyMappingRequest);
        return ResponseEntity.ok("난이도 매핑을 완료했습니다.");
    }

}
