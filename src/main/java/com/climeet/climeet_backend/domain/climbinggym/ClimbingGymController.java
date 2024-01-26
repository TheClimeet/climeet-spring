package com.climeet.climeet_backend.domain.climbinggym;

import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.AcceptedClimbingGymSimpleResponse;
import com.climeet.climeet_backend.domain.climbinggym.dto.ClimbingGymResponseDto.ClimbingGymSimpleResponse;
import com.climeet.climeet_backend.global.common.PageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ClimbingGym", description = "암장 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/gym")
public class ClimbingGymController {

    private final ClimbingGymService climbingGymService;

    @Operation(summary = "전국 암장 검색 기능(자체 목록화)")
    @GetMapping("/search/all")
    public ResponseEntity<PageResponseDto<List<ClimbingGymSimpleResponse>>> getClimbingGymSearchingList(
        @RequestParam("gymname") String gymName, @RequestParam int page, @RequestParam int size
    ) {
        return ResponseEntity.ok(climbingGymService.searchClimbingGym(gymName, page, size));
    }

    @Operation(summary = "Manager가 등록된 암장 검색 기능")
    @GetMapping("/search")
    public ResponseEntity<PageResponseDto<List<AcceptedClimbingGymSimpleResponse>>> getAcceptedClimbingGymSearchingList(
        @RequestParam("gymname") String gymName, @RequestParam int page, @RequestParam int size
    ) {
        return ResponseEntity.ok(climbingGymService.searchAcceptedClimbingGym(gymName, page, size));
    }

}