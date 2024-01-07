package com.climeet.climeet_backend.domain.routerecord;

import com.climeet.climeet_backend.domain.climbingrecord.ClimbingRecord;
import com.climeet.climeet_backend.domain.climbingrecord.dto.ClimbingRecordResponseDto.ClimbingRecordSimpleInfo;
import com.climeet.climeet_backend.domain.route.Route;
import com.climeet.climeet_backend.domain.route.RouteRepository;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordRequestDto;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordResponseDto;
import com.climeet.climeet_backend.domain.routerecord.dto.RouteRecordResponseDto.RouteRecordSimpleInfo;
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
            Route route = routeRepository.findById(requestDto.getRouteId()).orElseThrow();
            routeRecordRepository.save(RouteRecord.toEntity(requestDto, climbingRecord, route));
            Integer count = requestDto.getAttemptCount();

            climbingRecord.attemptCountUp(count);

            if (requestDto.getIsCompleted()) {
                climbingRecord.totalCompletedCountUp();
            }

            return ApiResponse.onSuccess("루트기록 성공");
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }

    public List<RouteRecordSimpleInfo> getRouteRecords() {
        try {
            List<RouteRecord> recordList = routeRecordRepository.findAll();
            return recordList.stream()
                .map(RouteRecordSimpleInfo::new)
                .toList();
        } catch (Exception e) {
            throw new GeneralException(ErrorStatus._BAD_REQUEST);
        }
    }

    public RouteRecordSimpleInfo getRouteRecord(Long id) {
        try {
            return new RouteRecordSimpleInfo(routeRecordRepository.findById(id).orElseThrow());
        } catch (Exception e){
            throw new RuntimeException();
        }
    }
}