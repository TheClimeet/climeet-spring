package com.climeet.climeet_backend.domain.routerecord;

import com.climeet.climeet_backend.domain.climbingrecord.ClimbingRecord;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.route.RouteRepository;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordResponseDto;
import com.climeet.climeet_backend.global.response.ApiResponse;
import com.climeet.climeet_backend.global.response.code.status.ErrorStatus;
import com.climeet.climeet_backend.global.response.exception.GeneralException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class RouteRecordService {

    private final RouteRecordRepository routeRecordRepository;
    private final RouteRepository routeRepository;

    @Transactional
    public ApiResponse<String> addRouteRecord(RouteRecordRequestDto requestDto,
        ClimbingRecord climbingRecord) {
        try {
            Route route = routeRepository.findById(requestDto.getRouteId()).get();
            routeRecordRepository.save(RouteRecord.toEntity(requestDto, climbingRecord, route));
            return ApiResponse.onSuccess("루트기록 성공");
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }
}