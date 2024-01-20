package com.climeet.climeet_backend.domain.climbinggym;

import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymSimpleResponse;
import com.climeet.climeet_backend.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ClimbingGym", description = "암장 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gym")
public class ClimbingGymController {

    private final ClimbingGymService climbingGymService;

    /**
     * [GET] 암장검색 (Manager가 등록이 되어있지 않은 암장도 조회)
     */
    @Operation(summary = "전국 암장 검색 기능(자체 목록화)")
    @GetMapping("/search/all/{word}")
    public ResponseEntity<List<ClimbingGymSimpleResponse>> getClimbingGymSearchingList(
        @PathVariable String word
    ) {
        return ResponseEntity.ok(climbingGymService.searchClimbingGym(word));
    }

    /**
     * [GET] 암장검색 (Manager가 등록되어있는 암장만 조회)
     */
    @Operation(summary = "Manager가 등록된 암장 검색 기능")
    @GetMapping("/search/{word}")
    public ResponseEntity<List<ClimbingGymSimpleResponse>> getAcceptedClimbingGymSearchingList(
        @PathVariable String word
    ) {
        return ResponseEntity.ok(climbingGymService.searchAcceptedClimbingGym(word));
    }

}